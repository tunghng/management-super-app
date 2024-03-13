package com.im.sso.dto.mapper;

import com.im.sso.dto.model.AppComponentDto;
import com.im.sso.model.AppComponent;
import org.mapstruct.Mapper;

@Mapper
public interface AppComponentMapper {

    AppComponent toModel(AppComponentDto componentDto);

    AppComponentDto toDto(AppComponent component);
}
