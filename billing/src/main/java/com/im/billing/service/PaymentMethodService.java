package com.im.billing.service;

import com.im.billing.dto.model.AppUserDto;
import com.im.billing.dto.model.PaymentMethodDto;
import com.im.billing.dto.response.page.PageData;
import com.im.billing.dto.response.page.PageLink;

import java.util.UUID;

public interface PaymentMethodService {
    PageData<PaymentMethodDto> findPaymentMethods(PageLink pageLink, UUID tenantId, Boolean isSearchMatchCase);

    PaymentMethodDto findByNameAndTenantId(String name, UUID tenantId);

    PaymentMethodDto findByIdAndTenantId(UUID paymentMethodId, AppUserDto currentUser);

    PaymentMethodDto save(PaymentMethodDto paymentMethodDto, AppUserDto currentUser);
}
