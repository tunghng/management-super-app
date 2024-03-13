package com.im.support.dto.mapper;

import com.im.support.dto.model.LogDto;
import com.im.support.model.Log;
import com.im.support.service.UserService;
import com.im.support.util.CommonUtils;
import org.mapstruct.Mapper;

@Mapper(uses = {UserService.class, CommonUtils.class})
public interface LogMapper {
    LogDto toDto(Log entity);
}
