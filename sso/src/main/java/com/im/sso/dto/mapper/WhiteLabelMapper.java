package com.im.sso.dto.mapper;

import com.im.sso.dto.model.WhiteLabelDto;
import com.im.sso.model.WhiteLabel;
import org.mapstruct.Mapper;

@Mapper
public interface WhiteLabelMapper {

    WhiteLabel toModel(WhiteLabelDto tenantWhiteLabelDto);

    WhiteLabelDto toDto(WhiteLabel whiteLabel);
}
