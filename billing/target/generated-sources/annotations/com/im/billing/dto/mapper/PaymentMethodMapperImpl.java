package com.im.billing.dto.mapper;

import com.im.billing.dto.model.PaymentMethodDto;
import com.im.billing.dto.model.PaymentMethodDto.PaymentMethodDtoBuilder;
import com.im.billing.model.PaymentMethod;
import java.time.ZoneOffset;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-10T09:23:27+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class PaymentMethodMapperImpl implements PaymentMethodMapper {

    @Override
    public PaymentMethodDto toDto(PaymentMethod paymentMethod) {
        if ( paymentMethod == null ) {
            return null;
        }

        PaymentMethodDtoBuilder paymentMethodDto = PaymentMethodDto.builder();

        paymentMethodDto.id( paymentMethod.getId() );
        paymentMethodDto.name( paymentMethod.getName() );
        paymentMethodDto.data( paymentMethod.getData() );
        paymentMethodDto.tenantId( paymentMethod.getTenantId() );
        if ( paymentMethod.getCreatedAt() != null ) {
            paymentMethodDto.createdAt( Date.from( paymentMethod.getCreatedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        if ( paymentMethod.getUpdatedAt() != null ) {
            paymentMethodDto.updatedAt( Date.from( paymentMethod.getUpdatedAt().toInstant( ZoneOffset.UTC ) ) );
        }

        return paymentMethodDto.build();
    }
}
