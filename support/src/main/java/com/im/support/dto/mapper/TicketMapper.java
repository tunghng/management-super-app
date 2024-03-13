package com.im.support.dto.mapper;

import com.im.support.dto.model.TicketDto;
import com.im.support.dto.model.TicketSaveDto;
import com.im.support.model.Ticket;
import com.im.support.service.ContactService;
import com.im.support.service.TicketTypeService;
import com.im.support.service.UserService;
import org.mapstruct.Mapper;

@Mapper(uses = {ContactService.class, TicketTypeService.class, UserService.class})
public interface TicketMapper {

    TicketDto toDto(Ticket ticket);

    TicketSaveDto toSaveDto(TicketDto ticketDto);
}
