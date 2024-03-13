package com.im.form.dto.mapper;

import com.im.form.dto.response.FormPageResponseDto;
import com.im.form.dto.response.FormResponseDto;
import com.im.form.model.Form;
import com.im.form.model.FormCode;
import java.time.ZoneOffset;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-30T10:56:05+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class FormMapperImpl implements FormMapper {

    @Override
    public FormResponseDto mapToResponseDto(Form form) {
        if ( form == null ) {
            return null;
        }

        FormResponseDto.FormResponseDtoBuilder formResponseDto = FormResponseDto.builder();

        formResponseDto.code( formCodeId( form ) );
        formResponseDto.isApproved( form.getIsApproved() );
        if ( form.getCreatedAt() != null ) {
            formResponseDto.createdAt( Date.from( form.getCreatedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        formResponseDto.createdBy( form.getCreatedBy() );

        formResponseDto.data( getAddition(form) );

        return formResponseDto.build();
    }

    @Override
    public FormPageResponseDto mapToPageResponseDto(Form form) {
        if ( form == null ) {
            return null;
        }

        FormPageResponseDto.FormPageResponseDtoBuilder formPageResponseDto = FormPageResponseDto.builder();

        formPageResponseDto.code( formCodeId( form ) );
        formPageResponseDto.id( form.getId() );
        formPageResponseDto.isRead( form.getIsRead() );
        if ( form.getCreatedAt() != null ) {
            formPageResponseDto.createdAt( Date.from( form.getCreatedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        formPageResponseDto.isApproved( form.getIsApproved() );

        return formPageResponseDto.build();
    }

    private String formCodeId(Form form) {
        if ( form == null ) {
            return null;
        }
        FormCode code = form.getCode();
        if ( code == null ) {
            return null;
        }
        String id = code.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
