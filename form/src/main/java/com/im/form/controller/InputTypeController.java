package com.im.form.controller;

import com.im.form.model.InputTypeTemplate;
import com.im.form.service.InputTypeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/form/inputType")
@AllArgsConstructor
public class InputTypeController extends BaseController {
    private final InputTypeService inputTypeService;

    @Operation(summary = "Get All Input Types")
    @GetMapping
    public ResponseEntity<List<InputTypeTemplate>> getDataTypes() {
        return ResponseEntity.ok(inputTypeService.getInputTypes());
    }
}
