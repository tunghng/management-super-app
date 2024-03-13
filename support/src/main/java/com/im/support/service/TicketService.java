package com.im.support.service;

import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.TicketDto;
import com.im.support.dto.model.TicketSaveDto;
import com.im.support.dto.response.page.PageData;
import com.im.support.dto.response.page.PageLink;
import com.im.support.model.enums.TicketState;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    PageData<TicketDto> findTickets(
            PageLink pageLink,
            TicketState state,
            List<UUID> typeIdList,
            List<UUID> contactIdList,
            Boolean isDeleted,
            Long createdAtStartTs,
            Long createdAtEndTs,
            Long updatedAtStartTs,
            Long updatedAtEndTs,
            AppUserDto currentUser,
            Boolean isSearchMatchCase
    );

    Long countUnreadTickets(
            AppUserDto currentUser,
            Boolean isRead,
            TicketState state
    );

    TicketDto save(TicketSaveDto ticketDto, AppUserDto currentUser);

    TicketDto closed(TicketDto ticketDto, AppUserDto currentUser);

    TicketDto findById(UUID ticketId, UUID tenantId);

    TicketDto findDetailById(UUID ticketId, AppUserDto currentUser);

    ResponseEntity<Object> exportData(
            List<String> attributeList,
            String fileName,
            String language,
            Long createdAtStartTs,
            Long createdAtEndTs,
            AppUserDto currentUser,
            List<UUID> contactIdList,
            List<UUID> typeIdList);

    String deleteTicket(UUID ticketId, AppUserDto currentUser);

    String restoreTicket(UUID ticketId, AppUserDto currentUser);

}
