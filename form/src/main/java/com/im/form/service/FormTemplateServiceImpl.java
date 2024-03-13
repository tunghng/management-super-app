package com.im.form.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.form.dto.mapper.ContactMapper;
import com.im.form.dto.mapper.FormTemplateMapper;
import com.im.form.dto.model.AppUserDto;
import com.im.form.dto.request.FormTemplateRequestDto;
import com.im.form.dto.response.CustomerFormTemplateDto;
import com.im.form.dto.response.FormTemplateDetailDto;
import com.im.form.dto.response.FormTemplateInfoDto;
import com.im.form.dto.response.GeneralFormTemplateDto;
import com.im.form.dto.response.page.PageData;
import com.im.form.dto.response.page.PageLink;
import com.im.form.exception.BadRequestException;
import com.im.form.exception.ForbiddenException;
import com.im.form.exception.NotFoundException;
import com.im.form.model.*;
import com.im.form.model.enums.InputType;
import com.im.form.model.enums.Role;
import com.im.form.repository.AdditionRepository;
import com.im.form.repository.ContactRepository;
import com.im.form.repository.FormRepository;
import com.im.form.repository.FormTemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FormTemplateServiceImpl implements FormTemplateService {
    private static final String USER_PERMISSION_ERROR = "You do not have permissions to do this action";
    private static final String FORM_TEMPLATE_NOT_FOUND = "Form template with id [%s] is not found";

    private final FormTemplateRepository formTemplateRepository;
    private final FormRepository formRepository;
    private final ContactRepository contactRepository;
    private final AdditionRepository additionRepository;
    private final ContactMapper contactMapper;
    private final FormTemplateMapper formTemplateMapper;

    @Override
    public PageData<GeneralFormTemplateDto> getGeneralFormTemplates(PageLink pageLink, AppUserDto appUserDto, Boolean isPublic, Boolean isDeleted, Boolean isSearchMatchCase) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize(), pageLink.toSort(pageLink.getSortOrder()));

        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<FormTemplate> formTemplate = formTemplateRepository
                .findGeneralTemplates(
                        appUserDto.getTenantId(),
                        isPublic,
                        isDeleted,
                        searchText,
                        isSearchMatchCase,
                        pageable
                );

        Page<GeneralFormTemplateDto> formTemplateResponsePage = formTemplate.map(f -> {
            GeneralFormTemplateDto general = formTemplateMapper.mapToGeneralDto(f);
            String link = "/form/inputForm/" + f.getId();
            if (f.getContactId() != null) {
                link += "?contactId=" + f.getContactId();
            }
            general.setPathUrl(link);
            general.setCount(formRepository.countByFormTemplateId(f.getId()));
            general.setCountUnread(formRepository.countByFormTemplateIdAndIsRead(f.getId(), false));
            general.setLatestUpdatedAt(formRepository.getLatestSendForm(f.getId()));
            return general;
        });
        return new PageData<>(formTemplateResponsePage);
    }

    @Override
    public PageData<CustomerFormTemplateDto> getCustomerFormTemplates(
            PageLink pageLink,
            AppUserDto appUserDto,
            Boolean isPublic,
            Boolean isDeleted,
            List<UUID> contactId,
            Boolean isSearchMatchCase
    ) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize(), pageLink.toSort(pageLink.getSortOrder()));
        List<UUID> theContactId = appUserDto.getRole().equals(Role.CUSTOMER)
                ? List.of(appUserDto.getContactId())
                : contactId;
        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<FormTemplate> formTemplate = formTemplateRepository
                .findCustomerTemplates(
                        appUserDto.getTenantId(),
                        theContactId,
                        isPublic,
                        isDeleted,
                        searchText,
                        isSearchMatchCase,
                        pageable);
        Page<CustomerFormTemplateDto> formTemplateResponsePage = formTemplate.map(f -> {
            CustomerFormTemplateDto customer = formTemplateMapper.mapToCustomerDto(f);
            customer.setContact(contactMapper.toInfoDto(contactRepository.findById(f.getContactId()).orElse(null)));
            String link = "/form/inputForm/" + f.getId();
            if (f.getContactId() != null) {
                link += "?contactId=" + f.getContactId();
            }
            customer.setPathUrl(link);
            customer.setCount(formRepository.countByFormTemplateId(f.getId()));
            customer.setCountUnread(formRepository.countByFormTemplateIdAndIsRead(f.getId(), false));
            customer.setLatestUpdatedAt(formRepository.getLatestSendForm(f.getId()));
            return customer;
        });
        return new PageData<>(formTemplateResponsePage);
    }

    @Override
    public FormTemplateInfoDto getFormTemplate(UUID formTemplateId, AppUserDto appUserDto, HttpServletRequest request) {
        FormTemplate formTemplate = formTemplateValidate(formTemplateId, appUserDto);
        FormTemplateInfoDto formTemplateInfoDto = formTemplateMapper.mapToInfoDto(formTemplate);
        String link = "/form/inputForm/" + formTemplate.getId();
        if (formTemplate.getContactId() != null) {
            link += "?contactId=" + formTemplate.getContactId();
        }
        formTemplateInfoDto.setPathUrl(link);
        formTemplateInfoDto.setCount(formRepository.countByFormTemplateId(formTemplate.getId()));
        formTemplateInfoDto.setLatestUpdatedAt(formRepository.getLatestSendForm(formTemplate.getId()));
        return formTemplateInfoDto;
    }

    @Override
    public FormTemplateDetailDto getFormTemplateDetail(UUID formTemplateId, AppUserDto appUserDto, HttpServletRequest request) {
        FormTemplate formTemplate = formTemplateValidate(formTemplateId, appUserDto);
        return formTemplateMapper.mapToDetailDto(formTemplate);
    }

    @Transactional
    @Override
    public FormTemplateInfoDto saveFormTemplate(FormTemplateRequestDto formTemplateRequestDto, AppUserDto appUserDto) {
        FormTemplate formTemplate = new FormTemplate();
        if (formTemplateRequestDto.getId() == null) {
            BeanUtils.copyProperties(formTemplateRequestDto, formTemplate, "additions");
            Collection<Addition> additions = formTemplateRequestDto.getAdditions();
            formatAddition(additions);
            formTemplate.setCode(new FormTemplateCode());
            formTemplate.setCreatedBy(appUserDto.getId());
            formTemplate.setUpdateBy(appUserDto.getId());
            formTemplate.setTenantId(appUserDto.getTenantId());
            formTemplate.setAdditions(additions);
            if (appUserDto.getRole().equals(Role.CUSTOMER)) {
                formTemplate.setContactId(appUserDto.getContactId());
            }
        } else {
            formTemplate = formTemplateValidate(
                    formTemplateRequestDto.getId(), appUserDto);
            List<UUID> additionIdList = formTemplate.getAdditions().stream().map(Addition::getId)
                    .collect(Collectors.toList());
            if (!additionIdList.isEmpty()) {
                additionRepository.deleteAllById(additionIdList);
            }
            BeanUtils.copyProperties(formTemplateRequestDto, formTemplate);
            formTemplate.setUpdateBy(appUserDto.getId());
        }
        FormTemplate savedFormTemplate = formTemplateRepository.saveAndFlush(formTemplate);
        return formTemplateMapper.mapToInfoDto(savedFormTemplate);
    }

    @Override
    @Transactional
    public void removeFormTemplate(UUID formTemplateId, AppUserDto appUserDto) {
        FormTemplate formTemplate = formTemplateValidate(formTemplateId, appUserDto);
        if (formTemplate.getIsDeleted()) {
            throw new BadRequestException("Cannot remove a form template that doesn't exist");
        }
        formTemplate.setIsDeleted(true);
        formTemplateRepository.save(formTemplate);
    }

    @Transactional
    @Override
    public void restoreFormTemplate(UUID formTemplateId, AppUserDto appUserDto) {
        FormTemplate formTemplate = formTemplateValidate(formTemplateId, appUserDto);
        if (!formTemplate.getIsDeleted()) {
            throw new BadRequestException("Cannot restore a form template that exists");
        }
        formTemplate.setIsDeleted(false);
        formTemplateRepository.save(formTemplate);
    }

    @Transactional
    @Override
    public String publicFormTemplate(UUID formTemplateId, AppUserDto appUserDto) {
        FormTemplate formTemplate = formTemplateValidate(formTemplateId, appUserDto);
        formTemplate.setIsPublic(!formTemplate.getIsPublic());
        formTemplateRepository.save(formTemplate);
        return formTemplate.getIsPublic() ? "public" : "private";
    }

    private void formatAddition(Collection<Addition> additions) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            for (Addition addition : additions) {
                Text text = addition.getInputText().getValue();
                if (text.getData() == null) {
                    text.setData("");
                }
                if (text.getValue() == null) {
                    text.setValue("");
                }
                text.setData(text.getData().strip());
                text.setValue(text.getValue().strip());
                if (addition.getInputText().getType().equals(InputType.RADIO)) {
                    List<String> data = Arrays.stream(mapper.readValue(text.getData(), String[].class)).map(t -> "\"" + t.strip().replace("\"", "\\\"") + "\"").toList();
                    text.setData(data.toString());
                } else if (addition.getInputText().getType().equals(InputType.SELECT)) {
                    List<String> data = Arrays.stream(mapper.readValue(text.getData(), String[].class)).map(t -> "\"" + t.strip().replace("\"", "\\\"") + "\"").toList();
                    text.setData(data.toString());
                    List<String> value = Arrays.stream(mapper.readValue(text.getValue(), String[].class)).map(t -> "\"" + t.strip().replace("\"", "\\\"") + "\"").toList();
                    text.setValue(value.toString());
                } else if (addition.getInputText().getType().equals(InputType.ATTACHED_FILE)) {
                    List<String> value = Arrays.stream(mapper.readValue(text.getValue(), String[].class)).map(t -> "\"" + t.strip().replace("\"", "\\\"") + "\"").toList();
                    text.setValue(value.toString());
                }
                addition.setInputText(
                        InputText.builder()
                                .type(addition.getInputText().getType())
                                .value(text)
                                .build());
            }
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }

    }

    private FormTemplate formTemplateValidate(UUID formTemplateId, AppUserDto appUserDto) {
        FormTemplate formTemplate = formTemplateRepository
                .findById(formTemplateId)
                .orElseThrow(() -> new NotFoundException(String.format(FORM_TEMPLATE_NOT_FOUND, formTemplateId)));
        if (appUserDto.getRole().equals(Role.CUSTOMER)) {
            if (!formTemplate.getTenantId().equals(appUserDto.getTenantId()) ||
                    (formTemplate.getContactId() != null
                            && !formTemplate.getContactId().equals(appUserDto.getContactId()))
            ) {
                throw new ForbiddenException(USER_PERMISSION_ERROR);
            }
        }
        return formTemplate;
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
