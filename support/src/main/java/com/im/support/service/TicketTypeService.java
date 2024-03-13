package com.im.support.service;

import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.TicketTypeDto;
import com.im.support.dto.response.page.PageData;
import com.im.support.dto.response.page.PageLink;
import com.im.support.model.TicketType;

import java.util.UUID;

public interface TicketTypeService {

    PageData<TicketTypeDto> findTicketTypes(PageLink pageLink, UUID tenantId);

    TicketTypeDto findByNameAndTenantId(String name, UUID tenantId);

    TicketTypeDto save(TicketTypeDto ticketTypeDto, AppUserDto currentUser);

    TicketType toModel(String type);

    String toString(TicketTypeDto type);
}
