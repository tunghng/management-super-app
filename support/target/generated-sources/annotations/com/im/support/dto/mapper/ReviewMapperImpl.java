package com.im.support.dto.mapper;

import com.im.support.dto.model.ReviewDto;
import com.im.support.model.Review;
import com.im.support.model.enums.ReviewTitle;
import com.im.support.service.UserService;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-08T16:12:37+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Autowired
    private UserService userService;

    @Override
    public ReviewDto toDto(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setId( review.getId() );
        if ( review.getTitle() != null ) {
            reviewDto.setTitle( review.getTitle().name() );
        }
        reviewDto.setBody( review.getBody() );
        reviewDto.setUpdatedAt( review.getUpdatedAt() );
        reviewDto.setUpdatedBy( userService.findByUserId( review.getUpdatedBy() ) );

        return reviewDto;
    }

    @Override
    public Review toModel(ReviewDto reviewDto) {
        if ( reviewDto == null ) {
            return null;
        }

        Review review = new Review();

        review.setUpdatedBy( toUserId( reviewDto.getUpdatedBy() ) );
        review.setId( reviewDto.getId() );
        review.setUpdatedAt( reviewDto.getUpdatedAt() );
        if ( reviewDto.getTitle() != null ) {
            review.setTitle( Enum.valueOf( ReviewTitle.class, reviewDto.getTitle() ) );
        }
        review.setBody( reviewDto.getBody() );

        return review;
    }
}
