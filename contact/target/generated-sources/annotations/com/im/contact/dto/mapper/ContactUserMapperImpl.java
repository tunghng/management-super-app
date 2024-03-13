package com.im.contact.dto.mapper;

import com.im.contact.dto.model.ContactUserDto;
import com.im.contact.model.ContactUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-07T14:24:14+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class ContactUserMapperImpl implements ContactUserMapper {

    @Override
    public ContactUser toModel(ContactUserDto contactUserDto) {
        if ( contactUserDto == null ) {
            return null;
        }

        ContactUser contactUser = new ContactUser();

        return contactUser;
    }

    @Override
    public ContactUserDto toDto(ContactUser contactUser) {
        if ( contactUser == null ) {
            return null;
        }

        ContactUserDto contactUserDto = new ContactUserDto();

        return contactUserDto;
    }
}
