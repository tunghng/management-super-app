package com.im.billing.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {
    private UUID id;
    private String code;
    private String title;
    private String description;
    private long price;
    private Boolean isDeleted;
    private String state;
    private BillTypeDto type;
    private UUID tenantId;
    private AppUserDto createdBy;
    private AppUserDto updatedBy;
    private AppUserDto closedBy;
    private AppUserDto paidBy;
    private long dueDateTs;
    private Date createdAt;
    private Date updatedAt;
    private Date closedAt;
    private Date paidAt;
    private Collection<String> attachedFile;
    private ContactDto contact;
    private Date dueDate;
}
