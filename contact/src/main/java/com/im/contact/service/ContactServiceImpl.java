package com.im.contact.service;

import com.im.contact.dao.ContactDao;
import com.im.contact.dto.mapper.AppUserMapper;
import com.im.contact.dto.mapper.ContactMapper;
import com.im.contact.dto.model.AppUserDetailDto;
import com.im.contact.dto.model.AppUserDto;
import com.im.contact.dto.model.ContactDto;
import com.im.contact.dto.model.ContactExportDto;
import com.im.contact.dto.response.page.PageData;
import com.im.contact.dto.response.page.PageLink;
import com.im.contact.exception.BadRequestException;
import com.im.contact.exception.ForbiddenException;
import com.im.contact.kafka.ContactProducer;
import com.im.contact.kafka.NotificationProducer;
import com.im.contact.model.Contact;
import com.im.contact.model.ContactUser;
import com.im.contact.model.enums.RoleType;
import com.im.contact.repository.AppUserRepository;
import com.im.contact.repository.ContactRepository;
import com.im.contact.repository.ContactUserRepository;
import com.im.contact.util.ExcelGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactProducer contactProducer;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ContactUserRepository contactUserRepository;

    @Autowired
    ContactDao contactDao;

    @Autowired
    ContactMapper contactMapper;

    @Autowired
    AppUserMapper userMapper;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    NotificationProducer notificationProducer;

    @Override
    public PageData<ContactDto> findContacts(
            PageLink pageLink,
            Boolean isDeleted,
            Long createdAtStartTs,
            Long createdAtEndTs,
            UUID tenantId,
            Boolean isSearchMatchCase
    ) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize(), pageLink.toSort(pageLink.getSortOrder()));

        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");
        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        isTimeStampValid(createdAtStartTs, createdAtEndTs);
        Page<Contact> contacts = contactRepository.findContacts(
                searchText,
                isSearchMatchCase,
                isDeleted,
                convertTimestampToDateTime(createdAtStartTs),
                convertTimestampToDateTime(createdAtEndTs),
                tenantId,
                pageable);
        Page<ContactDto> contactDtoList = contacts.map(contact -> contactMapper.toDto(contact));
        return new PageData<>(contactDtoList);
    }

    @Override
    public PageData<AppUserDetailDto> findContactUsers(PageLink pageLink, UUID tenantId, Boolean isSearchMatchCase) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());
        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<ContactUser> users = contactUserRepository.findByTenantId(
                searchText,
                isSearchMatchCase,
                tenantId,
                pageable);
        Page<AppUserDetailDto> userDtoList = users.map(contactUser -> {
            AppUserDetailDto dto = userMapper.toBaseDto(contactUser.getUser());
            dto.setContact(contactMapper.toDto(contactUser.getContact()));
            return dto;
        });
        return new PageData<>(userDtoList);
    }

    @Override
    public ContactDto findById(UUID contactId, UUID tenantId) {
        Contact contact = contactRepository.findByIdAndTenantId(contactId, tenantId).orElse(null);
        return contactMapper.toDto(contact);
    }

    @Override
    public String getAvatarById(UUID contactId) {
        Contact contact = contactRepository.findById(contactId).orElse(new Contact());
        return contact.getAvatar();
    }

    @Override
    public ResponseEntity<Object> exportContacts(
            List<String> attributes,
            Long startTs,
            Long endTs,
            String language,
            String filename,
            AppUserDto currentUser
    ) {
        try {
            if (currentUser.getRole().equalsIgnoreCase(RoleType.CUSTOMER.name())) {
                throw new ForbiddenException("You do not have permission to do this action.");
            }
            ContactExportDto contactExport = findByDynamicAttributes(attributes, startTs, endTs, currentUser.getTenantId());
            ExcelGenerator excelGenerator = new ExcelGenerator(contactExport, language);
            ByteArrayOutputStream outputStream = excelGenerator.generate();
            if (filename == null || filename.isEmpty()) {
                LocalDateTime ldt = Instant.ofEpochMilli(Instant.now().toEpochMilli())
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();
                filename = "Contacts_" + ldt.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
            }
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".xlsx")
                    .body(outputStream.toByteArray());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ContactExportDto findByDynamicAttributes(List<String> attributes, Long startTs, Long endTs, UUID tenantId) {
        isTimeStampValid(startTs, endTs);
        if (attributes == null || attributes.isEmpty()) {
            attributes = List.of("id", "name", "taxNumber", "email", "phone", "field");
        }
        LocalDateTime startDate = startTs != null
                ? Instant.ofEpochMilli(startTs).atZone(ZoneId.systemDefault()).toLocalDateTime()
                : contactRepository.findMinCreatedAtByTenantId(tenantId);
        LocalDateTime endDate = endTs != null
                ? Instant.ofEpochMilli(endTs).atZone(ZoneId.systemDefault()).toLocalDateTime()
                : contactRepository.findMaxCreatedAtByTenantId(tenantId);
        List<?> contacts = contactDao.findByDynamicAttributes(
                attributes,
                startDate,
                endDate,
                tenantId);
        List<Object[]> contactData = new ArrayList<>();
        if (attributes.size() == 1) {
            for (Object object : contacts) {
                contactData.add(new Object[]{object});
            }
        } else {
            contactData = (List<Object[]>) contacts;
        }
        return ContactExportDto.builder()
                .startTs(startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .endTs(endDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .totalAttributes(attributes.size())
                .totalElements(contacts.size())
                .attributes(attributes)
                .data(contactData)
                .build();
    }

    @Override
    public ContactDto save(ContactDto contactDto, UUID tenantId) {
        Contact contact = new Contact();
        if (contactDto.getId() != null) {
            contact = contactRepository.findById(contactDto.getId()).orElse(null);
        }
        assert contact != null;
        BeanUtils.copyProperties(contactDto, contact);
        contact.setTenantId(tenantId);
        contact.setIsDeleted(false);

        Contact savedContact = contactRepository.saveAndFlush(contact);
        ContactDto dto = contactMapper.toDto(savedContact);
        contactProducer.sendMessage(contactMapper.toDto(savedContact));
        return dto;
    }

    @Override
    public void delete(UUID contactId) {
        Contact contact = contactRepository.findById(contactId).orElse(null);
        assert contact != null;
        if (contact.getIsDeleted()) {
            throw new BadRequestException(
                    String.format("Contact with id [%s] has already deleted", contactId)
            );
        }
        contact.setIsDeleted(true);
        contactRepository.save(contact);
    }

    public void restore(UUID contactId) {
        Contact contact = contactRepository.findById(contactId).orElse(null);
        assert contact != null;
        if (!contact.getIsDeleted()) {
            throw new BadRequestException(
                    String.format("Contact with id [%s] has already displayed", contactId)
            );
        }
        contact.setIsDeleted(false);
        contactRepository.save(contact);
    }

    @Override
    public String syncContacts() {
        contactRepository.findAll().forEach(contact -> {
            contactProducer.sendMessage(contactMapper.toDto(contact));
        });
        return "Sync contacts successfully";
    }

    private void isTimeStampValid(Long startTs, Long endTs) {
        if (startTs != null || endTs != null) {
            if ((startTs == null || endTs == null) ||
                    (startTs > endTs)) {
                throw new BadRequestException("Invalid time range");
            }
        }
    }

    private LocalDateTime convertTimestampToDateTime(Long timestamp) {
        return timestamp != null ?
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
                : null;
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
