package com.im.form.service;

import com.im.form.dto.model.AppUserDto;
import com.im.form.dto.request.FormTemplateRequestDto;
import com.im.form.dto.response.CustomerFormTemplateDto;
import com.im.form.dto.response.FormTemplateDetailDto;
import com.im.form.dto.response.FormTemplateInfoDto;
import com.im.form.dto.response.GeneralFormTemplateDto;
import com.im.form.dto.response.page.PageData;
import com.im.form.dto.response.page.PageLink;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface FormTemplateService {
    PageData<GeneralFormTemplateDto> getGeneralFormTemplates(
            PageLink pageLink,
            AppUserDto appUserDto,
            Boolean isPublic,
            Boolean isDeleted,
            Boolean isSearchMatchCase
    );

    PageData<CustomerFormTemplateDto> getCustomerFormTemplates(
            PageLink pageLink,
            AppUserDto appUserDto,
            Boolean isPublic,
            Boolean isDeleted,
            List<UUID> contactId,
            Boolean isSearchMatchCase
    );

    FormTemplateInfoDto saveFormTemplate(FormTemplateRequestDto formTemplateRequestDto, AppUserDto appUserDto);

    FormTemplateInfoDto getFormTemplate(UUID formTemplateId, AppUserDto appUserDto, HttpServletRequest request);

    void removeFormTemplate(UUID formTemplateId, AppUserDto appUserDto);

    void restoreFormTemplate(UUID formTemplateId, AppUserDto appUserDto);

    String publicFormTemplate(UUID formTemplateId, AppUserDto appUserDto);

    FormTemplateDetailDto getFormTemplateDetail(UUID formTemplateId, AppUserDto appUserDto, HttpServletRequest request);

}
