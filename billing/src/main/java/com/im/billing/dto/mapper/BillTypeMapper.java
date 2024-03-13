package com.im.billing.dto.mapper;

import com.im.billing.dto.model.BillTypeDto;
import com.im.billing.model.BillType;
import org.mapstruct.Mapper;

@Mapper
public interface BillTypeMapper {
    BillTypeDto toDto(BillType billType);

    BillType toModel(BillTypeDto billTypeDto);
}
