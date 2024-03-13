package com.im.form.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.form.dto.mapper.ContactMapper;
import com.im.form.dto.mapper.FormMapper;
import com.im.form.dto.mapper.FormTemplateMapper;
import com.im.form.dto.model.AdditionDto;
import com.im.form.dto.model.AppUserDto;
import com.im.form.dto.model.InputTextDto;
import com.im.form.dto.model.TextDto;
import com.im.form.dto.request.FileInfoRequestDto;
import com.im.form.dto.request.FormRequestDto;
import com.im.form.dto.response.FormEmptyDto;
import com.im.form.dto.response.FormPageResponseDto;
import com.im.form.dto.response.FormResponseDto;
import com.im.form.dto.response.page.PageData;
import com.im.form.dto.response.page.PageLink;
import com.im.form.exception.BadRequestException;
import com.im.form.exception.ForbiddenException;
import com.im.form.exception.NotFoundException;
import com.im.form.kafka.FileProducer;
import com.im.form.model.Form;
import com.im.form.model.FormCode;
import com.im.form.model.FormTemplate;
import com.im.form.model.enums.InputType;
import com.im.form.model.enums.Role;
import com.im.form.repository.ContactRepository;
import com.im.form.repository.FormRepository;
import com.im.form.repository.FormTemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class FormServiceImpl implements FormService {
    private static final String FORM_NOT_FOUND = "Form with id [%s] is not found";
    private static final String FORM_TEMPLATE_NOT_FOUND = "Form template with id [%s] is not found";
    private static final String FORM_TEMPLATE_CONTACT_NOT_FOUND =
            "Form template with id [%s] and contact with id [%s] is not found";
    private static final String USER_PERMISSION_ERROR = "You do not have permissions to do this action";
    private static final String FORM_TEMPLATE_ACCESS_ERROR =
            "You do not have permission to access private form template";

    private final FormRepository formRepository;
    private final FormTemplateRepository formTemplateRepository;
    private final ContactRepository contactRepository;
    private final FormTemplateMapper formTemplateMapper;
    private final FormMapper formMapper;
    private final ContactMapper contactMapper;
    private final FileProducer fileProducer;

    @Override
    public PageData<FormPageResponseDto> getForms(PageLink pageLink, UUID formTemplateId, AppUserDto appUserDto) {
        FormTemplate formTemplate = formTemplateRepository
                .findById(formTemplateId)
                .orElseThrow(() -> new NotFoundException(String.format(FORM_TEMPLATE_NOT_FOUND, formTemplateId)));
        if (!formTemplate.getTenantId().equals(appUserDto.getTenantId())) {
            throw new ForbiddenException(USER_PERMISSION_ERROR);
        }
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize(), pageLink.toSort(pageLink.getSortOrder()));

        Page<Form> form = formRepository.findByTemplate(formTemplate, pageable);
        Page<FormPageResponseDto> formPageResponsePage = form.map(f -> {
            FormPageResponseDto formPage = formMapper.mapToPageResponseDto(f);
            formPage.setPathUrl("/form/result/" + f.getId());
            if (f.getContactId() != null) {
                formPage.setContact(contactMapper.toInfoDto(contactRepository.findById(f.getContactId()).orElse(null)));
            }
            return formPage;
        });
        return new PageData<>(formPageResponsePage);
    }

    @Override
    public FormResponseDto getFilledForm(UUID formId, AppUserDto appUserDto) {
        Form form = formRepository
                .findById(formId)
                .orElseThrow(() -> new NotFoundException(String.format(FORM_NOT_FOUND, formId)));
        FormTemplate formTemplate = form.getTemplate();
        if (appUserDto != null && !formTemplate.getIsDeleted()) {
            if (formTemplate.getTenantId().equals(appUserDto.getTenantId()) && !form.getIsRead()) {
                form.setIsRead(true);
                formRepository.save(form);
            }
        }
        FormResponseDto formResponseDto = formMapper.mapToResponseDto(form);
        formResponseDto.setFormTemplateInfo(formTemplateMapper.mapToShortInfoDto(formTemplate));
        return formResponseDto;
    }

    @Override
    public FormEmptyDto getEmptyForm(UUID formTemplateId, UUID contactId) {
        FormTemplate formTemplate = formTemplateRepository
                .findById(formTemplateId)
                .orElseThrow(() -> new NotFoundException(String.format(FORM_TEMPLATE_NOT_FOUND, formTemplateId)));
        if (contactId != null) {
            if (!contactId.equals(formTemplate.getContactId())) {
                throw new NotFoundException(String.format(FORM_TEMPLATE_CONTACT_NOT_FOUND, formTemplateId, contactId));
            }
        }
        if (!formTemplate.getIsPublic()) {
            throw new BadRequestException(FORM_TEMPLATE_ACCESS_ERROR);
        }

        return formTemplateMapper.mapToEmptyFromDto(formTemplate);
    }

    @Transactional
    @Override
    public UUID saveForm(FormRequestDto formRequestDto, AppUserDto appUserDto) {
        List<UUID> attachedFile = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            FormTemplate formTemplate = formTemplateRepository
                    .findById(formRequestDto.getFormTemplateId())
                    .orElseThrow(() ->
                            new NotFoundException(String.format(FORM_TEMPLATE_NOT_FOUND, formRequestDto.getFormTemplateId())));
            if (!formTemplate.getIsPublic()) {
                throw new BadRequestException(FORM_TEMPLATE_ACCESS_ERROR);
            }
            Form form = new Form();
            form.setTemplate(formTemplate);
            Collection<AdditionDto> additions = formRequestDto.getData();
            formatAddition(additions);
            checkValidAddition(additions);
            for (AdditionDto addition : additions) {
                if (addition.getInputText().getType().equals(InputType.ATTACHED_FILE)) {
                    TextDto text = addition.getInputText().getValue();
                    if (!text.getValue().isEmpty()) {
                        attachedFile = Arrays.stream(mapper.readValue(text.getValue(), String[].class))
                                .map(UUID::fromString).collect(Collectors.toList());
                    }
                }
            }
            form.setCode(new FormCode());
            form.setData(mapper.writeValueAsString(additions));
            if (appUserDto != null) {
                form.setCreatedBy(appUserDto.getId());
                if (appUserDto.getRole().equals(Role.CUSTOMER)) {
                    form.setContactId(appUserDto.getContactId());
                }
            }
            formRepository.save(form);
            if (!attachedFile.isEmpty()) {
                for (UUID fileId : attachedFile) {
                    fileProducer.sendMessage(FileInfoRequestDto.builder()
                            .id(fileId)
                            .tenantId(formTemplate.getTenantId())
                            .userId(formTemplate.getCreatedBy())
                            .isPublished(true)
                            .build());
                }
            }
            return form.getId();
        } catch (Exception exc) {
//            if (!attachedFile.isEmpty()) {
//                for (UUID fileId : attachedFile) {
//                    fileProducer.removeFile(fileId);
//                }
//            }
            throw new RuntimeException(exc.getMessage());
        }
    }

    @Override
    public void approveForm(UUID formId, AppUserDto appUserDto) {
        Form form = formValidate(formId, appUserDto);
        if (!form.getIsApproved()) {
            form.setApprovedAt(new Date());
            form.setApprovedBy(appUserDto.getId());
        } else {
            form.setApprovedAt(null);
            form.setApprovedBy(null);
        }
        form.setIsApproved(!form.getIsApproved());
        form.setIsRead(true);
        formRepository.save(form);
    }

    private void formatAddition(Collection<AdditionDto> additions) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(additions);
        for (AdditionDto addition : additions) {
            TextDto text = addition.getInputText().getValue();
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
                    InputTextDto.builder()
                            .type(addition.getInputText().getType())
                            .value(text)
                            .build());
        }
    }

    private void checkValidAddition(Collection<AdditionDto> additions) throws JsonProcessingException {
        for (AdditionDto addition : additions) {
            ObjectMapper mapper = new ObjectMapper();

            // Validate required field
            if (addition.getTitle().isEmpty()) {
                throw new BadRequestException("Title is required");
            }
            if (addition.getInputText().getType() == null) {
                throw new BadRequestException("Invalid input");
            }
            if (addition.getRequired()) {
                TextDto text = addition.getInputText().getValue();
                if (text.getValue().isEmpty()) {
                    throw new BadRequestException("The field with title `" + addition.getTitle() + "` is required");
                } else {
                    if (addition.getInputText().getType().equals(InputType.SELECT) ||
                            addition.getInputText().getType().equals(InputType.ATTACHED_FILE)) {
                        List<String> value = Arrays.stream(mapper.readValue(text.getValue(), String[].class)).map(String::strip).toList();
                        if (value.isEmpty()) {
                            throw new BadRequestException("The field with title `" + addition.getTitle() + "` is required");
                        }
                    }
                }
            }

            // Validate valid value
            if (addition.getInputText().getType().equals(InputType.RADIO)) {
                TextDto text = addition.getInputText().getValue();
                List<String> data = Arrays.stream(mapper.readValue(text.getData(), String[].class)).map(String::strip).toList();
                String value = text.getValue();
                if (!value.isEmpty() && !data.contains(value)) {
                    throw new BadRequestException(String.format("Invalid value for radio input type [%s]. " +
                            "It should be %s", value, data));
                }
            } else if (addition.getInputText().getType().equals(InputType.SELECT)) {
                TextDto text = addition.getInputText().getValue();
                List<String> data = Arrays.stream(mapper.readValue(text.getData(), String[].class)).map(String::strip).toList();
                List<String> value = Arrays.stream(mapper.readValue(text.getValue(), String[].class)).map(String::strip).toList();
                for (String val : value) {
                    if (!data.contains(val)) {
                        throw new BadRequestException(String.format("Invalid value for select input type [%s]. " +
                                "It should be %s", val, data));
                    }
                }
            }
        }
    }

    private Form formValidate(UUID formId, AppUserDto appUserDto) {
        Form form = formRepository
                .findById(formId)
                .orElseThrow(() -> new NotFoundException(String.format(FORM_NOT_FOUND, formId)));
        FormTemplate formTemplate = form.getTemplate();
        if (appUserDto.getRole().equals(Role.CUSTOMER)) {
            if (!formTemplate.getTenantId().equals(appUserDto.getTenantId()) ||
                    (formTemplate.getContactId() != null
                            && !formTemplate.getContactId().equals(appUserDto.getContactId()))
            ) {
                throw new ForbiddenException(USER_PERMISSION_ERROR);
            }
        }
        return form;
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
