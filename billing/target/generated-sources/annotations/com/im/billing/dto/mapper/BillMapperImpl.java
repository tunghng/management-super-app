package com.im.billing.dto.mapper;

import com.im.billing.dto.model.BillDto;
import com.im.billing.dto.model.BillTypeDto;
import com.im.billing.dto.model.BillTypeDto.BillTypeDtoBuilder;
import com.im.billing.dto.model.ContactDto;
import com.im.billing.model.Bill;
import com.im.billing.model.BillType;
import com.im.billing.model.Contact;
import com.im.billing.service.UserService;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-10T09:23:27+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class BillMapperImpl implements BillMapper {

    @Autowired
    private UserService userService;

    @Override
    public BillDto toDto(Bill bill) {
        if ( bill == null ) {
            return null;
        }

        BillDto billDto = new BillDto();

        billDto.setId( bill.getId() );
        billDto.setCode( bill.getCode() );
        billDto.setTitle( bill.getTitle() );
        billDto.setDescription( bill.getDescription() );
        billDto.setPrice( bill.getPrice() );
        billDto.setIsDeleted( bill.getIsDeleted() );
        if ( bill.getState() != null ) {
            billDto.setState( bill.getState().name() );
        }
        billDto.setType( billTypeToBillTypeDto( bill.getType() ) );
        billDto.setTenantId( bill.getTenantId() );
        billDto.setCreatedBy( userService.findByUserId( bill.getCreatedBy() ) );
        billDto.setUpdatedBy( userService.findByUserId( bill.getUpdatedBy() ) );
        billDto.setPaidBy( userService.findByUserId( bill.getPaidBy() ) );
        if ( bill.getCreatedAt() != null ) {
            billDto.setCreatedAt( Date.from( bill.getCreatedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        if ( bill.getUpdatedAt() != null ) {
            billDto.setUpdatedAt( Date.from( bill.getUpdatedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        if ( bill.getClosedAt() != null ) {
            billDto.setClosedAt( Date.from( bill.getClosedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        if ( bill.getPaidAt() != null ) {
            billDto.setPaidAt( Date.from( bill.getPaidAt().toInstant( ZoneOffset.UTC ) ) );
        }
        Collection<String> collection = bill.getAttachedFile();
        if ( collection != null ) {
            billDto.setAttachedFile( new ArrayList<String>( collection ) );
        }
        billDto.setContact( contactToContactDto( bill.getContact() ) );
        if ( bill.getDueDate() != null ) {
            billDto.setDueDate( Date.from( bill.getDueDate().toInstant( ZoneOffset.UTC ) ) );
        }

        return billDto;
    }

    protected BillTypeDto billTypeToBillTypeDto(BillType billType) {
        if ( billType == null ) {
            return null;
        }

        BillTypeDtoBuilder billTypeDto = BillTypeDto.builder();

        billTypeDto.id( billType.getId() );
        billTypeDto.name( billType.getName() );
        billTypeDto.tenantId( billType.getTenantId() );
        if ( billType.getCreatedAt() != null ) {
            billTypeDto.createdAt( Date.from( billType.getCreatedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        if ( billType.getUpdatedAt() != null ) {
            billTypeDto.updatedAt( Date.from( billType.getUpdatedAt().toInstant( ZoneOffset.UTC ) ) );
        }

        return billTypeDto.build();
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
}
