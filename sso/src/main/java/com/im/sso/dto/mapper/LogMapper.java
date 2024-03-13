package com.im.sso.dto.mapper;

import com.im.sso.dto.model.LogDto;
import com.im.sso.model.Log;
import com.im.sso.service.UserMapperService;
import com.im.sso.util.CommonUtils;
import org.mapstruct.Mapper;

@Mapper(uses = {CommonUtils.class, UserMapperService.class})
public interface LogMapper {
    LogDto toDto(Log entity);
}
