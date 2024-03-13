package com.im.billing.service;

import com.im.billing.dto.model.AppUserDto;
import com.im.billing.dto.model.BillTypeDto;
import com.im.billing.dto.response.page.PageData;
import com.im.billing.dto.response.page.PageLink;
import com.im.billing.model.BillType;

import java.util.UUID;

public interface BillTypeService {
    PageData<BillTypeDto> findBillTypes(PageLink pageLink, AppUserDto currentUser, Boolean isSearchMatchCase);

    BillTypeDto findByNameAndTenantId(String name, AppUserDto currentUser);

    BillTypeDto findById(UUID billTypeId, AppUserDto currentUser);

    BillTypeDto save(BillTypeDto billTypeDto, AppUserDto currentUser);

    BillType toBillType(String type);

    String toString(BillType billType);
}
