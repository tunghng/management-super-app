package com.im.billing.dto.mapper;

import com.im.billing.dto.model.BillDto;
import com.im.billing.model.Bill;
import com.im.billing.service.BillService;
import com.im.billing.service.BillTypeService;
import com.im.billing.service.PaymentMethodService;
import com.im.billing.service.UserService;
import org.mapstruct.Mapper;

@Mapper(uses = {BillTypeService.class, UserService.class, BillService.class, PaymentMethodService.class})
public interface BillMapper {
    BillDto toDto(Bill bill);
}
