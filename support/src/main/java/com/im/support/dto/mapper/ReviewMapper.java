package com.im.support.dto.mapper;

import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.ReviewDto;
import com.im.support.model.Review;
import com.im.support.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(uses = {UserService.class})
public interface ReviewMapper {

    ReviewDto toDto(Review review);

    @Mapping(source = "updatedBy", target = "updatedBy", qualifiedByName = "toUserId")
    Review toModel(ReviewDto reviewDto);

    @Named("toUserId")
    default UUID toUserId(AppUserDto userDto) {
        return userDto.getId();
    }
}
