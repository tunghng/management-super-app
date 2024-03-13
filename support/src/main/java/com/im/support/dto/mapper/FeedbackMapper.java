package com.im.support.dto.mapper;

import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.FeedbackDto;
import com.im.support.model.Feedback;
import com.im.support.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(uses = {UserService.class})
public interface FeedbackMapper {

    FeedbackDto toDto(Feedback feedback);

    @Mapping(source = "updatedBy", target = "updatedBy", qualifiedByName = "toUserId")
    Feedback toModel(FeedbackDto feedbackDto);

    @Named("toUserId")
    default UUID toUserId(AppUserDto userDto) {
        return userDto.getId();
    }
}
