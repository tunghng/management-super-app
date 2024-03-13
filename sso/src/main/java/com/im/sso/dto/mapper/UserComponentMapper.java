package com.im.sso.dto.mapper;

import com.im.sso.dto.model.UserComponentDto;
import com.im.sso.model.UserComponent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface UserComponentMapper {

    @Mapping(source = "userComponent", target = "userId", qualifiedByName = "userId")
    @Mapping(source = "userComponent", target = "componentName", qualifiedByName = "componentName")
    UserComponentDto toDto(UserComponent userComponent);

    @Named("userId")
    default UUID appUserToUserId(UserComponent component) {
        return component.getUser().getId();
    }

    @Named("componentName")
    default String componentToComponentName(UserComponent component) {
        return component.getAppComponent().getName();
    }
}
