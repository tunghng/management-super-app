package com.im.billing.dto.mapper;

import com.im.billing.dto.model.BillTypeDto;
import com.im.billing.dto.model.BillTypeDto.BillTypeDtoBuilder;
import com.im.billing.model.BillType;
import com.im.billing.model.BillType.BillTypeBuilder;
import java.time.ZoneOffset;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-08T10:40:29+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class BillTypeMapperImpl implements BillTypeMapper {

    @Override
    public BillTypeDto toDto(BillType billType) {
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

    @Override
    public BillType toModel(BillTypeDto billTypeDto) {
        if ( billTypeDto == null ) {
            return null;
        }

        BillTypeBuilder billType = BillType.builder();

        billType.name( billTypeDto.getName() );
        billType.tenantId( billTypeDto.getTenantId() );

        return billType.build();
    }
}
