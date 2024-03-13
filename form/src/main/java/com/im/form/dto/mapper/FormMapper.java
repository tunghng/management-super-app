package com.im.form.dto.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.form.dto.model.AdditionDto;
import com.im.form.dto.response.FormPageResponseDto;
import com.im.form.dto.response.FormResponseDto;
import com.im.form.exception.MapperException;
import com.im.form.model.Form;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;


@Mapper(componentModel = "spring")
public interface FormMapper {
    @Mapping(target = "code", source = "form.code.id")
    @Mapping(target = "data", expression = "java(getAddition(form))")
    @Mapping(target = "formTemplateInfo", ignore = true)
    FormResponseDto mapToResponseDto(Form form);

    @Mapping(target = "code", source = "form.code.id")
    @Mapping(target = "pathUrl", ignore = true)
    @Mapping(target = "contact", ignore = true)
    FormPageResponseDto mapToPageResponseDto(Form form);

    default Collection<AdditionDto> getAddition(Form form) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Collection<AdditionDto>> typeRef = new TypeReference<>() {
        };
        try {
            return mapper.readValue(form.getData(), typeRef);
        } catch (JsonProcessingException exc) {
            throw new MapperException(exc.getMessage());
        }
    }
}
