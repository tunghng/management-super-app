package com.im.contact.service;

import com.im.contact.dto.model.AppUserDto;
import com.im.contact.dto.model.ContactDto;
import com.im.contact.dto.response.page.PageData;
import com.im.contact.dto.response.page.PageLink;

import java.util.UUID;

public interface ContactUserService {
    void save(ContactDto contactDto, AppUserDto userDto);

    PageData<AppUserDto> findUsersByContactId(PageLink pageLink, UUID contactId, Boolean isSearchMatchCase);
}
