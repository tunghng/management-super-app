package com.im.billing.dto.mapper;

import com.im.billing.dto.model.PaymentMethodDto;
import com.im.billing.model.PaymentMethod;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentMethodMapper {
    PaymentMethodDto toDto(PaymentMethod paymentMethod);
}
