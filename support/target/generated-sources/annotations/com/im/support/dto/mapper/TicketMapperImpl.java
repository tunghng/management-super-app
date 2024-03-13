package com.im.support.dto.mapper;

import com.im.support.dto.model.ContactDto;
import com.im.support.dto.model.ReviewShortDto;
import com.im.support.dto.model.TicketDto;
import com.im.support.dto.model.TicketSaveDto;
import com.im.support.dto.model.TicketTypeDto;
import com.im.support.model.Contact;
import com.im.support.model.Ticket;
import com.im.support.model.TicketReview;
import com.im.support.model.TicketType;
import com.im.support.model.enums.TicketState;
import com.im.support.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T15:39:10+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class TicketMapperImpl implements TicketMapper {

    @Autowired
    private UserService userService;

    @Override
    public TicketDto toDto(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }

        TicketDto ticketDto = new TicketDto();

        ticketDto.setId( ticket.getId() );
        ticketDto.setTitle( ticket.getTitle() );
        ticketDto.setCode( ticket.getCode() );
        ticketDto.setDescription( ticket.getDescription() );
        if ( ticket.getPriority() != null ) {
            ticketDto.setPriority( ticket.getPriority().name() );
        }
        if ( ticket.getState() != null ) {
            ticketDto.setState( ticket.getState().name() );
        }
        ticketDto.setIsDeleted( ticket.getIsDeleted() );
        ticketDto.setTenantId( ticket.getTenantId() );
        ticketDto.setCreatedBy( userService.findByUserId( ticket.getCreatedBy() ) );
        ticketDto.setUpdatedBy( userService.findByUserId( ticket.getUpdatedBy() ) );
        ticketDto.setCreatedAt( ticket.getCreatedAt() );
        ticketDto.setUpdatedAt( ticket.getUpdatedAt() );
        ticketDto.setClosedAt( ticket.getClosedAt() );
        Collection<String> collection = ticket.getAttachedFile();
        if ( collection != null ) {
            ticketDto.setAttachedFile( new ArrayList<String>( collection ) );
        }
        ticketDto.setContact( contactToContactDto( ticket.getContact() ) );
        ticketDto.setReview( ticketReviewToReviewShortDto( ticket.getReview() ) );
        ticketDto.setType( ticketTypeToTicketTypeDto( ticket.getType() ) );

        return ticketDto;
    }

    @Override
    public TicketSaveDto toSaveDto(TicketDto ticketDto) {
        if ( ticketDto == null ) {
            return null;
        }

        TicketSaveDto ticketSaveDto = new TicketSaveDto();

        ticketSaveDto.setId( ticketDto.getId() );
        ticketSaveDto.setTitle( ticketDto.getTitle() );
        ticketSaveDto.setDescription( ticketDto.getDescription() );
        ticketSaveDto.setPriority( ticketDto.getPriority() );
        if ( ticketDto.getState() != null ) {
            ticketSaveDto.setState( Enum.valueOf( TicketState.class, ticketDto.getState() ) );
        }
        Collection<String> collection = ticketDto.getAttachedFile();
        if ( collection != null ) {
            ticketSaveDto.setAttachedFile( new ArrayList<String>( collection ) );
        }

        return ticketSaveDto;
    }

    protected ContactDto contactToContactDto(Contact contact) {
        if ( contact == null ) {
            return null;
        }

        ContactDto contactDto = new ContactDto();

        contactDto.setId( contact.getId() );
        contactDto.setName( contact.getName() );
        contactDto.setTaxNumber( contact.getTaxNumber() );
        contactDto.setEmail( contact.getEmail() );
        contactDto.setPhone( contact.getPhone() );
        contactDto.setField( contact.getField() );
        contactDto.setDescription( contact.getDescription() );
        contactDto.setAvatar( contact.getAvatar() );
        contactDto.setIsDeleted( contact.getIsDeleted() );
        contactDto.setTenantId( contact.getTenantId() );
        contactDto.setCreatedAt( contact.getCreatedAt() );
        contactDto.setUpdatedAt( contact.getUpdatedAt() );

        return contactDto;
    }

    protected ReviewShortDto ticketReviewToReviewShortDto(TicketReview ticketReview) {
        if ( ticketReview == null ) {
            return null;
        }

        ReviewShortDto reviewShortDto = new ReviewShortDto();

        return reviewShortDto;
    }

    protected TicketTypeDto ticketTypeToTicketTypeDto(TicketType ticketType) {
        if ( ticketType == null ) {
            return null;
        }

        TicketTypeDto ticketTypeDto = new TicketTypeDto();

        ticketTypeDto.setName( ticketType.getName() );
        ticketTypeDto.setId( ticketType.getId() );
        ticketTypeDto.setTenantId( ticketType.getTenantId() );

        return ticketTypeDto;
    }
}
