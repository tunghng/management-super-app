package com.im.form.dto.mapper;

import com.im.form.dto.response.*;
import com.im.form.model.FormTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FormTemplateMapper {
    @Mapping(target = "code", source = "formTemplate.code.id")
    GeneralFormTemplateDto mapToGeneralDto(FormTemplate formTemplate);

    @Mapping(target = "code", source = "formTemplate.code.id")
    CustomerFormTemplateDto mapToCustomerDto(FormTemplate formTemplate);

    @Mapping(target = "code", source = "formTemplate.code.id")
    FormTemplateInfoDto mapToInfoDto(FormTemplate formTemplate);

    @Mapping(target = "code", source = "formTemplate.code.id")
    FormTemplateShortInfo mapToShortInfoDto(FormTemplate formTemplate);

    FormTemplateDetailDto mapToDetailDto(FormTemplate formTemplate);

    FormEmptyDto mapToEmptyFromDto(FormTemplate formTemplate);
}
