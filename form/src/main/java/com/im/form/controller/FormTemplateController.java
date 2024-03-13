package com.im.form.controller;

import com.im.form.dto.model.AppUserDto;
import com.im.form.dto.request.FormTemplateRequestDto;
import com.im.form.dto.response.*;
import com.im.form.dto.response.page.PageData;
import com.im.form.dto.response.page.PageLink;
import com.im.form.service.FormTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/form/template")
@AllArgsConstructor
public class FormTemplateController extends BaseController {
    private final FormTemplateService formTemplateService;

    @Operation(summary = "Get General Form Templates")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("general")
    public ResponseEntity<PageData<GeneralFormTemplateDto>> getGeneralFormTemplates(
            @Parameter(description = "Maximum amount of entities in a one page")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sequence number of page starting from 0")
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "Filter by public state")
            @RequestParam(required = false) Boolean isPublic,
            @Parameter(description = "Unavailable")
            @RequestParam(defaultValue = "") String searchText,
            @Parameter(description = "Property of entity to sort by")
            @RequestParam(defaultValue = "updatedAt") String sortProperty,
            @Parameter(description = "Sort order. ASC (ASCENDING) or DESC (DESCENDING)")
            @RequestParam(defaultValue = "DESC") String sortOrder,
            @Parameter(description = "Filter by deleted state")
            @RequestParam(defaultValue = "false") Boolean isDeleted,
            @Parameter(description = "Search Match Case Or Not")
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request) {
        AppUserDto appUserDto = getCurrentUser(request);
        PageLink pageLink = createPageLink(page, pageSize, searchText, sortProperty, sortOrder);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(formTemplateService.getGeneralFormTemplates(
                        pageLink,
                        appUserDto,
                        isPublic,
                        isDeleted,
                        isSearchMatchCase
                ));
    }

    @Operation(summary = "Get Customer Form Templates")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("customer")
    public ResponseEntity<PageData<CustomerFormTemplateDto>> getCustomerFormTemplates(
            @Parameter(description = "Maximum amount of entities in a one page")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sequence number of page starting from 0")
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "Filter by public state")
            @RequestParam(required = false) Boolean isPublic,
            @Parameter(description = "Filter by List of Contact Id")
            @RequestParam(required = false) List<UUID> contactId,
            @Parameter(description = "Unavailable")
            @RequestParam(defaultValue = "") String searchText,
            @Parameter(description = "Property of entity to sort by")
            @RequestParam(defaultValue = "updatedAt") String sortProperty,
            @Parameter(description = "Sort order. ASC (ASCENDING) or DESC (DESCENDING)")
            @RequestParam(defaultValue = "DESC") String sortOrder,
            @Parameter(description = "Filter by deleted state")
            @RequestParam(defaultValue = "false") Boolean isDeleted,
            @Parameter(description = "Search Match Case Or Not")
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request) {
        AppUserDto appUserDto = getCurrentUser(request);
        PageLink pageLink = createPageLink(page, pageSize, searchText, sortProperty, sortOrder);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(formTemplateService.getCustomerFormTemplates(
                        pageLink,
                        appUserDto,
                        isPublic,
                        isDeleted,
                        contactId,
                        isSearchMatchCase
                ));
    }

    @Operation(summary = "Get Form Template By Id")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("{formTemplateId}/shortInfo")
    public ResponseEntity<FormTemplateInfoDto> getFormTemplateById(
            @PathVariable("formTemplateId") UUID formTemplateId,
            HttpServletRequest request) {
        AppUserDto appUserDto = getCurrentUser(request);
        return ResponseEntity.ok(
                formTemplateService.getFormTemplate(formTemplateId, appUserDto, request)
        );
    }

    @Operation(summary = "Get Form Template By Id To Edit")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("{formTemplateId}")
    public ResponseEntity<FormTemplateDetailDto> getFormTemplateDetailById(
            @PathVariable("formTemplateId") UUID formTemplateId,
            HttpServletRequest request) {
        AppUserDto appUserDto = getCurrentUser(request);
        return ResponseEntity.ok(
                formTemplateService.getFormTemplateDetail(formTemplateId, appUserDto, request)
        );
    }

    @Operation(summary = "Save Form Templates")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<FormTemplateInfoDto> saveFormTemplate(
            @Valid @RequestBody FormTemplateRequestDto formTemplateRequestDto,
            HttpServletRequest request) {
        AppUserDto appUserDto = getCurrentUser(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(formTemplateService.saveFormTemplate(formTemplateRequestDto, appUserDto));
    }

    @Operation(summary = "Remove Form Template By Id")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("{formTemplateId}/public")
    public ResponseEntity<Response> removeFormTemplate(
            @PathVariable("formTemplateId") UUID formTemplateId,
            HttpServletRequest request) {
        AppUserDto appUserDto = getCurrentUser(request);
        formTemplateService.removeFormTemplate(formTemplateId, appUserDto);
        return ResponseEntity.ok(
                new Response(String.format("Form Template with id [%s] delete successful", formTemplateId))
        );
    }

    @Operation(summary = "Restore Form Template By Id")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("{formTemplateId}/public/restore")
    public ResponseEntity<Response> restoreFormTemplate(
            @PathVariable("formTemplateId") UUID formTemplateId,
            HttpServletRequest request) {
        AppUserDto appUserDto = getCurrentUser(request);
        formTemplateService.restoreFormTemplate(formTemplateId, appUserDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(HttpStatus.OK.value(), String.format("Form Template with id [%s] restore successful", formTemplateId)));
    }

    @Operation(summary = "Public Form Template By Id")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("{formTemplateId}/public")
    public ResponseEntity<Response> publicFormTemplate(
            @PathVariable("formTemplateId") UUID formTemplateId,
            HttpServletRequest request) {
        AppUserDto appUserDto = getCurrentUser(request);
        String status = formTemplateService.publicFormTemplate(formTemplateId, appUserDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(HttpStatus.OK.value(),
                        String.format("Form Template with id [%s] has been [%s] successful", formTemplateId, status)));
    }
}
