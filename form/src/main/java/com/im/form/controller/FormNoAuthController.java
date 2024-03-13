package com.im.form.controller;

import com.im.form.dto.request.FormRequestDto;
import com.im.form.dto.response.FormEmptyDto;
import com.im.form.dto.response.FormResponseDto;
import com.im.form.dto.response.ResponseId;
import com.im.form.service.FormService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/noauth/form")
@AllArgsConstructor
public class FormNoAuthController extends BaseController {
    private final FormService formService;

    @Operation(summary = "Get Filled Form By Form Id")
    @GetMapping("{formId}")
    public ResponseEntity<FormResponseDto> getFormById(
            @PathVariable("formId") UUID formId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(formService.getFilledForm(formId, null));
    }

    @Operation(summary = "Get Empty Form For Initialization")
    @GetMapping
    public ResponseEntity<FormEmptyDto> getEmptyForm(
            @RequestParam(value = "formTemplateId") UUID formTemplateId,
            @RequestParam(value = "contactId", required = false) UUID contactId) {
        return ResponseEntity
                .ok(formService.getEmptyForm(formTemplateId, contactId));
    }

    @Operation(summary = "Save A New Form")
    @PostMapping
    public ResponseEntity<ResponseId> saveForm(
            @Valid @RequestBody FormRequestDto formRequestDto) {
        UUID formId = formService.saveForm(formRequestDto, null);

        return ResponseEntity
                .ok(new ResponseId(formId));
    }
}
