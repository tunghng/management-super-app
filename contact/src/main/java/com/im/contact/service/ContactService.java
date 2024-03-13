package com.im.contact.service;

import com.im.contact.dto.model.AppUserDetailDto;
import com.im.contact.dto.model.AppUserDto;
import com.im.contact.dto.model.ContactDto;
import com.im.contact.dto.model.ContactExportDto;
import com.im.contact.dto.response.page.PageData;
import com.im.contact.dto.response.page.PageLink;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ContactService {

    PageData<ContactDto> findContacts(
            PageLink pageLink,
            Boolean isDeleted,
            Long createdAtStartTs,
            Long createdAtEndTs,
            UUID tenantId,
            Boolean isSearchMatchCase
    );

    PageData<AppUserDetailDto> findContactUsers(PageLink pageLink, UUID tenantId, Boolean isSearchMatchCase);

    ContactDto findById(UUID contactId, UUID tenantId);

    String getAvatarById(UUID contactId);

    ResponseEntity<Object> exportContacts(
            List<String> attributes,
            Long startTs,
            Long endTs,
            String language,
            String filename,
            AppUserDto currentUser
    );

    ContactExportDto findByDynamicAttributes(
            List<String> attributes,
            Long startTs,
            Long endTs,
            UUID tenantId
    );

    ContactDto save(ContactDto contactDto, UUID tenantId);

    void delete(UUID contactId);

    void restore(UUID contactId);

    String syncContacts();
}
