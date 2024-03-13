package com.im.support.dto.mapper;

import com.im.support.dto.model.TicketTypeDto;
import com.im.support.model.TicketType;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-08T16:12:37+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class TicketTypeMapperImpl implements TicketTypeMapper {

    @Override
    public TicketType toModel(TicketTypeDto ticketTypeDto) {
        if ( ticketTypeDto == null ) {
            return null;
        }

        TicketType ticketType = new TicketType();

        ticketType.setId( ticketTypeDto.getId() );
        ticketType.setName( ticketTypeDto.getName() );
        ticketType.setTenantId( ticketTypeDto.getTenantId() );

        return ticketType;
    }

    @Override
    public TicketTypeDto toDto(TicketType type) {
        if ( type == null ) {
            return null;
        }

        TicketTypeDto ticketTypeDto = new TicketTypeDto();

        ticketTypeDto.setName( type.getName() );
        ticketTypeDto.setId( type.getId() );
        ticketTypeDto.setTenantId( type.getTenantId() );

        return ticketTypeDto;
    }
}
