package com.im.form.service;


import com.im.form.dto.model.AppUserDto;
import com.im.form.dto.request.FormRequestDto;
import com.im.form.dto.response.FormEmptyDto;
import com.im.form.dto.response.FormPageResponseDto;
import com.im.form.dto.response.FormResponseDto;
import com.im.form.dto.response.page.PageData;
import com.im.form.dto.response.page.PageLink;

import java.util.UUID;

public interface FormService {
    PageData<FormPageResponseDto> getForms(PageLink pageLink, UUID formTemplateId, AppUserDto appUserDto);

    FormResponseDto getFilledForm(UUID formId, AppUserDto appUserDto);

    FormEmptyDto getEmptyForm(UUID formTemplateId, UUID contactId);

    UUID saveForm(FormRequestDto formRequestDto, AppUserDto userInfoDto);

    void approveForm(UUID formId, AppUserDto appUserDto);
}
