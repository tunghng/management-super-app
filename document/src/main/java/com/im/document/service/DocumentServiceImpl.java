package com.im.document.service;

import com.im.document.dto.mapper.ContactMapper;
import com.im.document.dto.mapper.DocumentMapper;
import com.im.document.dto.model.AppUserDto;
import com.im.document.dto.model.DocumentDto;
import com.im.document.dto.model.DocumentSaveDto;
import com.im.document.dto.response.page.PageData;
import com.im.document.dto.response.page.PageLink;
import com.im.document.exception.BadRequestException;
import com.im.document.exception.NotFoundException;
import com.im.document.model.Contact;
import com.im.document.model.Document;
import com.im.document.model.DocumentCode;
import com.im.document.model.DocumentType;
import com.im.document.model.enums.RoleType;
import com.im.document.repository.ContactRepository;
import com.im.document.repository.DocumentCodeRepository;
import com.im.document.repository.DocumentRepository;
import com.im.document.repository.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.Normalizer;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final DocumentCodeRepository documentCodeRepository;

    private final DocumentTypeRepository documentTypeRepository;

    private final ContactRepository contactRepository;

    private final DocumentMapper documentMapper;

    private final ContactMapper contactMapper;

    private final ContactService contactService;


    @Override
    public PageData<DocumentDto> findDocuments(
            PageLink pageLink,
            boolean isDeleted,
            List<UUID> typeIdList,
            List<UUID> contactIdList,
            AppUserDto currentUser,
            Boolean isSearchMatchCase
    ) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize(),
                pageLink.toSort(pageLink.getSortOrder()));
        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<Document> documentPage = !RoleType.CUSTOMER.equals(RoleType.lookup(currentUser.getRole()))
                ? documentRepository.findDocuments(
                searchText,
                isSearchMatchCase,
                isDeleted,
                typeIdList,
                contactIdList,
                currentUser.getTenantId(),
                pageable
        ) : documentRepository.findDocumentsByContactId(
                searchText,
                isSearchMatchCase,
                isDeleted,
                currentUser.getContactId(),
                pageable
        );
        Page<DocumentDto> documentDtoPage = documentPage.map(documentMapper::toDto);
        return new PageData<DocumentDto>(documentDtoPage);
    }

    @Override
    public DocumentDto findById(UUID documentId, UUID tenantId) {
        Document document = documentRepository.findByIdAndTenantId(documentId, tenantId);
        return document != null ? documentMapper.toDto(document) : null;
    }

    @Override
    @Transactional
    public DocumentDto save(DocumentSaveDto documentDto, AppUserDto currentUser) {

        Document document = documentDto.getId() != null ?
                documentRepository.findById(documentDto.getId())
                        .orElseThrow(() -> new NotFoundException(String.format("Document with id [%s] is not exist", documentDto.getId())))
                : new Document();

        BeanUtils.copyProperties(documentDto, document);

        Contact contact = contactMapper.toModel(
                contactService.findById(
                        documentDto.getContactId() != null ? documentDto.getContactId() : currentUser.getContactId()
                )
        );

        if (documentDto.getId() == null) {
            document.setCreatedBy(currentUser.getId());
            String code = documentCodeRepository.save(new DocumentCode()).getId();
            document.setCode(code);
        }

        DocumentType type = document.getType();
        if (documentDto.getType() != null) { // will deleted
            type = documentTypeRepository.findByNameAndTenantId(
                    documentDto.getType(), currentUser.getTenantId()
            );
        }
        if (documentDto.getTypeId() != null) {
            type = documentTypeRepository.findByIdAndTenantId(
                    documentDto.getTypeId(), currentUser.getTenantId()
            );
        }
        document.setType(type);

        document.setContact(contact);
        document.setUpdatedBy(currentUser.getId());
        document.setTenantId(currentUser.getTenantId());

        Document savedDocument = documentRepository.saveAndFlush(document);

        return documentMapper.toDto(savedDocument);
    }

    @Override
    @Transactional
    public String deleteDocumentById(UUID documentId) {
        Document document = documentRepository
                .findById(documentId)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Document with id [%s] is not exist", documentId))
                );
        if (document.getIsDeleted()) {
            throw new BadRequestException(String.format("Document with id [%s] has already deleted", documentId));
        }
        document.setIsDeleted(true);

        documentRepository.save(document);
        return String.format("Document with id [%s] has deleted successful", documentId);
    }

    @Override
    @Transactional
    public String restoreDocumentById(UUID documentId) {
        Document document = documentRepository
                .findById(documentId)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Document with id [%s] is not exist", documentId))
                );
        if (!document.getIsDeleted()) {
            throw new BadRequestException(String.format("Document with id [%s] has already displayed", documentId));
        }
        document.setIsDeleted(false);

        documentRepository.save(document);
        return String.format("Document with id [%s] has restored successful", documentId);
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
