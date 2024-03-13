package com.im.support.dto.mapper;

import com.im.support.dto.model.FeedbackDto;
import com.im.support.model.Feedback;
import com.im.support.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-08T16:12:37+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class FeedbackMapperImpl implements FeedbackMapper {

    @Autowired
    private UserService userService;

    @Override
    public FeedbackDto toDto(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }

        FeedbackDto feedbackDto = new FeedbackDto();

        feedbackDto.setId( feedback.getId() );
        feedbackDto.setTitle( feedback.getTitle() );
        feedbackDto.setBody( feedback.getBody() );
        feedbackDto.setUpdatedAt( feedback.getUpdatedAt() );
        feedbackDto.setUpdatedBy( userService.findByUserId( feedback.getUpdatedBy() ) );
        Collection<String> collection = feedback.getAttachedFile();
        if ( collection != null ) {
            feedbackDto.setAttachedFile( new ArrayList<String>( collection ) );
        }

        return feedbackDto;
    }

    @Override
    public Feedback toModel(FeedbackDto feedbackDto) {
        if ( feedbackDto == null ) {
            return null;
        }

        Feedback feedback = new Feedback();

        feedback.setUpdatedBy( toUserId( feedbackDto.getUpdatedBy() ) );
        feedback.setId( feedbackDto.getId() );
        feedback.setUpdatedAt( feedbackDto.getUpdatedAt() );
        feedback.setTitle( feedbackDto.getTitle() );
        feedback.setBody( feedbackDto.getBody() );
        Collection<String> collection = feedbackDto.getAttachedFile();
        if ( collection != null ) {
            feedback.setAttachedFile( new ArrayList<String>( collection ) );
        }

        return feedback;
    }
}
