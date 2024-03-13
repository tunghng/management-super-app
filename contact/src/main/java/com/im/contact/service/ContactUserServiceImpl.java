package com.im.contact.service;

import com.im.contact.dto.mapper.AppUserMapper;
import com.im.contact.dto.model.AppUserDto;
import com.im.contact.dto.model.ContactDto;
import com.im.contact.dto.response.page.PageData;
import com.im.contact.dto.response.page.PageLink;
import com.im.contact.model.AppUser;
import com.im.contact.model.Contact;
import com.im.contact.model.ContactUser;
import com.im.contact.repository.AppUserRepository;
import com.im.contact.repository.ContactRepository;
import com.im.contact.repository.ContactUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ContactUserServiceImpl implements ContactUserService {

    @Autowired
    ContactRepository contactRepository;


    @Autowired
    AppUserRepository userRepository;

    @Autowired
    ContactUserRepository contactUserRepository;


    @Autowired
    AppUserMapper userMapper;

    @Override
    public void save(ContactDto contactDto, AppUserDto userDto) {
        ContactUser existRelation = contactUserRepository.findByContactIdAndUserId(
                contactDto.getId(), userDto.getId());
        Contact contact = contactRepository.findById(contactDto.getId()).orElse(null);
        AppUser user = userRepository.findById(userDto.getId()).orElse(null);
        ContactUser contactUser = existRelation != null ? existRelation
                : new ContactUser(contact, user);
        contactUserRepository.saveAndFlush(contactUser);
    }

    @Override
    public PageData<AppUserDto> findUsersByContactId(PageLink pageLink, UUID contactId, Boolean isSearchMatchCase) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());
        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<ContactUser> contactUserList = contactUserRepository.findByContactId(
                searchText,
                isSearchMatchCase,
                contactId,
                pageable);
        Page<AppUserDto> userDtoPage = contactUserList.map(
                contactUser -> userMapper.toDto(contactUser.getUser())
        );
        return new PageData<>(userDtoPage);
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD); Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
