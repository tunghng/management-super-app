package com.im.form.controller;

import com.im.form.dto.model.AppUserDto;
import com.im.form.dto.request.FormRequestDto;
import com.im.form.dto.response.*;
import com.im.form.dto.response.page.PageData;
import com.im.form.dto.response.page.PageLink;
import com.im.form.service.FormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/form")
@AllArgsConstructor
public class FormController extends BaseController {
    private final FormService formService;

    @Operation(summary = "Get Forms By Form Template Id")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("template/{formTemplateId}/form")
    public ResponseEntity<PageData<FormPageResponseDto>> getForms(
            @PathVariable("formTemplateId") UUID formTemplateId,
            @Parameter(description = "Maximum amount of entities in a one page")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sequence number of page starting from 0")
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "Property of entity to sort by")
            @RequestParam(defaultValue = "createdAt") String sortProperty,
            @Parameter(description = "Sort order. ASC (ASCENDING) or DESC (DESCENDING)")
            @RequestParam(defaultValue = "DESC") String sortOrder,
            HttpServletRequest request) {
        AppUserDto appUserDto = getCurrentUser(request);
        PageLink pageLink = createPageLink(page, pageSize, "", sortProperty, sortOrder);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(formService.getForms(
                        pageLink,
                        formTemplateId,
                        appUserDto
                ));
    }

    @Operation(summary = "Get Filled Form By Form Id")
    @GetMapping("{formId}")
    public ResponseEntity<FormResponseDto> getFormById(
            @PathVariable("formId") UUID formId,
            HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(formService.getFilledForm(formId, getCurrentUserOrNull(request)));
    }

    @Operation(summary = "Get Empty Form For Initialization")
    @GetMapping
    public ResponseEntity<FormEmptyDto> getEmptyForm(
            @RequestParam(value = "formTemplateId") UUID formTemplateId,
            @RequestParam(value = "contactId", required = false) UUID contactId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(formService.getEmptyForm(formTemplateId, contactId));
    }

    @Operation(summary = "Save A New Form")
    @PostMapping
    public ResponseEntity<ResponseId> saveForm(
            @Valid @RequestBody FormRequestDto formRequestDto,
            HttpServletRequest request) {
        UUID formId = formService.saveForm(formRequestDto, getCurrentUserOrNull(request));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseId(formId));
    }

    @Operation(summary = "Approve Filled Form By Id")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("{formId}/approve")
    public ResponseEntity<Response> removeForm(
            @PathVariable("formId") UUID formId,
            HttpServletRequest request) {
        AppUserDto appUserDto = getCurrentUser(request);
        formService.approveForm(formId, appUserDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(HttpStatus.OK.value(), String.format("Form with id [%s] approved successful", formId)));
    }
}
