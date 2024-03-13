package com.im.support.dto.mapper;

import com.im.support.dto.model.TicketTypeDto;
import com.im.support.model.TicketType;
import org.mapstruct.Mapper;

@Mapper
public interface TicketTypeMapper {

    TicketType toModel(TicketTypeDto ticketTypeDto);

    TicketTypeDto toDto(TicketType type);
}
