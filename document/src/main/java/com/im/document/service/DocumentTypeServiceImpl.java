package com.im.document.service;

import com.im.document.dto.mapper.DocumentTypeMapper;
import com.im.document.dto.model.DocumentTypeDto;
import com.im.document.dto.response.page.PageData;
import com.im.document.dto.response.page.PageLink;
import com.im.document.exception.BadRequestException;
import com.im.document.model.DocumentType;
import com.im.document.repository.DocumentTypeRepository;
import org.springframework.beans.BeanUtils;
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
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final static String TYPE_NAME_ALREADY_EXIST = "Document type with name [%s] is already exist";

    @Autowired
    DocumentTypeRepository documentTypeRepository;

    @Autowired
    DocumentTypeMapper documentTypeMapper;

    @Override
    public PageData<DocumentTypeDto> findDocumentTypes(
            PageLink pageLink,
            UUID tenantId,
            Boolean isSearchMatchCase
    ) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize(),
                pageLink.toSort(pageLink.getSortOrder()));
        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<DocumentType> documentTypePage = documentTypeRepository.findDocumentTypes(
                searchText,
                isSearchMatchCase,
                tenantId,
                pageable
        );
        Page<DocumentTypeDto> documentTypeDtoPage = documentTypePage.map(documentTypeMapper::toDto);
        return new PageData<DocumentTypeDto>(documentTypeDtoPage);
    }

    @Override
    public DocumentTypeDto save(DocumentTypeDto documentTypeDto, UUID tenantId) {
        DocumentType type = documentTypeDto.getId() != null
                ? documentTypeMapper.toModel(documentTypeDto)
                : new DocumentType();

        checkIfNameExist(documentTypeDto.getName(), tenantId);

        BeanUtils.copyProperties(documentTypeDto, type);
        type.setTenantId(tenantId);
        DocumentType savedDocumentType = documentTypeRepository.save(type);
        return documentTypeMapper.toDto(savedDocumentType);
    }

    @Override
    public DocumentType toModel(String type) {
        return documentTypeRepository.findByName(type);
    }

    @Override
    public String toString(DocumentType type) {
        return type != null ? type.getName() : null;
    }

    private void checkIfNameExist(String name, UUID tenantId) {
        DocumentType type = documentTypeRepository.findByNameAndTenantId(name, tenantId);
        if (type != null) {
            throw new BadRequestException(
                    String.format(TYPE_NAME_ALREADY_EXIST, name)
            );
        }
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD); Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
