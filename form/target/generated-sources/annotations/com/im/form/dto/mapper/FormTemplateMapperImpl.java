package com.im.form.dto.mapper;

import com.im.form.dto.model.AdditionDto;
import com.im.form.dto.model.InputTextDto;
import com.im.form.dto.model.TextDto;
import com.im.form.dto.response.CustomerFormTemplateDto;
import com.im.form.dto.response.FormEmptyDto;
import com.im.form.dto.response.FormTemplateDetailDto;
import com.im.form.dto.response.FormTemplateInfoDto;
import com.im.form.dto.response.FormTemplateShortInfo;
import com.im.form.dto.response.GeneralFormTemplateDto;
import com.im.form.model.Addition;
import com.im.form.model.FormTemplate;
import com.im.form.model.FormTemplateCode;
import com.im.form.model.InputText;
import com.im.form.model.Text;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-30T10:56:05+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class FormTemplateMapperImpl implements FormTemplateMapper {

    @Override
    public GeneralFormTemplateDto mapToGeneralDto(FormTemplate formTemplate) {
        if ( formTemplate == null ) {
            return null;
        }

        GeneralFormTemplateDto.GeneralFormTemplateDtoBuilder generalFormTemplateDto = GeneralFormTemplateDto.builder();

        generalFormTemplateDto.code( formTemplateCodeId( formTemplate ) );
        generalFormTemplateDto.id( formTemplate.getId() );
        generalFormTemplateDto.name( formTemplate.getName() );
        generalFormTemplateDto.isPublic( formTemplate.getIsPublic() );
        generalFormTemplateDto.updatedAt( formTemplate.getUpdatedAt() );

        return generalFormTemplateDto.build();
    }

    @Override
    public CustomerFormTemplateDto mapToCustomerDto(FormTemplate formTemplate) {
        if ( formTemplate == null ) {
            return null;
        }

        CustomerFormTemplateDto.CustomerFormTemplateDtoBuilder customerFormTemplateDto = CustomerFormTemplateDto.builder();

        customerFormTemplateDto.code( formTemplateCodeId( formTemplate ) );
        customerFormTemplateDto.id( formTemplate.getId() );
        customerFormTemplateDto.name( formTemplate.getName() );
        customerFormTemplateDto.isPublic( formTemplate.getIsPublic() );
        customerFormTemplateDto.updatedAt( formTemplate.getUpdatedAt() );

        return customerFormTemplateDto.build();
    }

    @Override
    public FormTemplateInfoDto mapToInfoDto(FormTemplate formTemplate) {
        if ( formTemplate == null ) {
            return null;
        }

        FormTemplateInfoDto.FormTemplateInfoDtoBuilder formTemplateInfoDto = FormTemplateInfoDto.builder();

        formTemplateInfoDto.code( formTemplateCodeId( formTemplate ) );
        formTemplateInfoDto.id( formTemplate.getId() );
        formTemplateInfoDto.name( formTemplate.getName() );
        formTemplateInfoDto.isPublic( formTemplate.getIsPublic() );
        formTemplateInfoDto.isDeleted( formTemplate.getIsDeleted() );
        formTemplateInfoDto.description( formTemplate.getDescription() );
        formTemplateInfoDto.contactId( formTemplate.getContactId() );

        return formTemplateInfoDto.build();
    }

    @Override
    public FormTemplateShortInfo mapToShortInfoDto(FormTemplate formTemplate) {
        if ( formTemplate == null ) {
            return null;
        }

        FormTemplateShortInfo.FormTemplateShortInfoBuilder formTemplateShortInfo = FormTemplateShortInfo.builder();

        formTemplateShortInfo.code( formTemplateCodeId( formTemplate ) );
        formTemplateShortInfo.id( formTemplate.getId() );
        formTemplateShortInfo.name( formTemplate.getName() );
        formTemplateShortInfo.description( formTemplate.getDescription() );
        formTemplateShortInfo.logo( formTemplate.getLogo() );
        formTemplateShortInfo.contactId( formTemplate.getContactId() );

        return formTemplateShortInfo.build();
    }

    @Override
    public FormTemplateDetailDto mapToDetailDto(FormTemplate formTemplate) {
        if ( formTemplate == null ) {
            return null;
        }

        FormTemplateDetailDto.FormTemplateDetailDtoBuilder formTemplateDetailDto = FormTemplateDetailDto.builder();

        formTemplateDetailDto.id( formTemplate.getId() );
        formTemplateDetailDto.name( formTemplate.getName() );
        formTemplateDetailDto.description( formTemplate.getDescription() );
        formTemplateDetailDto.logo( formTemplate.getLogo() );
        formTemplateDetailDto.additions( additionCollectionToAdditionDtoCollection( formTemplate.getAdditions() ) );

        return formTemplateDetailDto.build();
    }

    @Override
    public FormEmptyDto mapToEmptyFromDto(FormTemplate formTemplate) {
        if ( formTemplate == null ) {
            return null;
        }

        FormEmptyDto.FormEmptyDtoBuilder formEmptyDto = FormEmptyDto.builder();

        formEmptyDto.name( formTemplate.getName() );
        formEmptyDto.description( formTemplate.getDescription() );
        formEmptyDto.logo( formTemplate.getLogo() );
        formEmptyDto.additions( additionCollectionToAdditionDtoCollection( formTemplate.getAdditions() ) );
        formEmptyDto.isDeleted( formTemplate.getIsDeleted() );

        return formEmptyDto.build();
    }

    private String formTemplateCodeId(FormTemplate formTemplate) {
        if ( formTemplate == null ) {
            return null;
        }
        FormTemplateCode code = formTemplate.getCode();
        if ( code == null ) {
            return null;
        }
        String id = code.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected TextDto textToTextDto(Text text) {
        if ( text == null ) {
            return null;
        }

        TextDto.TextDtoBuilder textDto = TextDto.builder();

        textDto.data( text.getData() );
        textDto.value( text.getValue() );

        return textDto.build();
    }

    protected InputTextDto inputTextToInputTextDto(InputText inputText) {
        if ( inputText == null ) {
            return null;
        }

        InputTextDto.InputTextDtoBuilder inputTextDto = InputTextDto.builder();

        inputTextDto.type( inputText.getType() );
        inputTextDto.value( textToTextDto( inputText.getValue() ) );

        return inputTextDto.build();
    }

    protected AdditionDto additionToAdditionDto(Addition addition) {
        if ( addition == null ) {
            return null;
        }

        AdditionDto.AdditionDtoBuilder additionDto = AdditionDto.builder();

        additionDto.inputText( inputTextToInputTextDto( addition.getInputText() ) );
        additionDto.title( addition.getTitle() );
        additionDto.description( addition.getDescription() );
        additionDto.required( addition.getRequired() );

        return additionDto.build();
    }

    protected Collection<AdditionDto> additionCollectionToAdditionDtoCollection(Collection<Addition> collection) {
        if ( collection == null ) {
            return null;
        }

        Collection<AdditionDto> collection1 = new ArrayList<AdditionDto>( collection.size() );
        for ( Addition addition : collection ) {
            collection1.add( additionToAdditionDto( addition ) );
        }

        return collection1;
    }
}
