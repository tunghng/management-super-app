package com.im.billing.service;

import com.im.billing.dto.model.AppUserDto;
import com.im.billing.dto.model.BillDto;
import com.im.billing.dto.model.BillSaveDto;
import com.im.billing.dto.response.page.PageData;
import com.im.billing.dto.response.page.PageLink;
import com.im.billing.model.enums.BillState;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface BillService {
    PageData<BillDto> findBills(
            PageLink pageLink,
            List<BillState> state,
            List<UUID> typeIdList,
            Long createdAtStartTs,
            Long createdAtEndTs,
            Long dueDateStartTs,
            Long dueDateEndTs,
            Long updatedAtStartTs,
            Long updatedAtEndTs,
            AppUserDto currentUser,
            List<UUID> contactIdList,
            Boolean isDeleted,
            Boolean isSearchMatchCase
    );

    BillDto save(BillSaveDto billDto, AppUserDto currentUser);

    BillDto findById(UUID billId, UUID tenantId);

    BillDto closeBill(UUID billId, AppUserDto currentUser);

    BillDto payBill(UUID billId, AppUserDto currentUser);

    ResponseEntity<Object> exportData(
            List<String> attributeList,
            String fileName,
            String language,
            Long createdAtStartTs,
            Long createdAtEndTs,
            AppUserDto currentUser,
            UUID contactId,
            BillState state,
            UUID typeId,
            UUID fromContactId
    );

    BillDto findDetailById(UUID billId, AppUserDto currentUser);

    void delete(UUID billId, AppUserDto currentUser);

    void restore(UUID billId, AppUserDto currentUser);

    long toMilliseconds(Date date);
}
