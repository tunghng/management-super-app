package com.im.contact.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUserDto {
    private UUID contactId;
    private List<AppUserDto> userDtoList;
}
