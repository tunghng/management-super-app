package com.im.billing.controller;

import com.im.billing.dto.model.BillTypeDto;
import com.im.billing.dto.response.page.PageData;
import com.im.billing.dto.response.page.PageLink;
import com.im.billing.service.BillTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/billing/type")
public class BillTypeController extends BaseController {
    @Autowired
    BillTypeService billTypeService;

    @GetMapping
    @Operation(summary = "Get bill types (getBillTypes)",
            description = "Retrieve a list of bill types with optional filtering and pagination.")
    public PageData<BillTypeDto> getBillTypes(
            @Parameter(description = "Page number for pagination")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page for pagination")
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "Search text for filtering bill types by name")
            @RequestParam(required = false) String searchText,
            @Parameter(description = "Search Match Case Or Not")
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText, null, null);
        return billTypeService.findBillTypes(pageLink, getCurrentUser(request), isSearchMatchCase);
    }

    @GetMapping("{typeId}")
    @Operation(summary = "Get bill type (getBillType)",
            description = "Retrieve a bill type by its ID.")
    public BillTypeDto getBillType(
            @Parameter(description = "ID of the bill type to retrieve")
            @PathVariable("typeId") UUID billTypeId,
            HttpServletRequest request
    ) {
        return billTypeService.findById(billTypeId, getCurrentUser(request));
    }

    @PostMapping
    @Operation(summary = "Create or update bill type (saveBillType)",
            description = "Create or update bill type. For creating bill type, remove the id field.")
    public BillTypeDto saveBillType(
            @Valid @RequestBody BillTypeDto billTypeDto,
            HttpServletRequest request
    ) {
        return billTypeService.save(billTypeDto, getCurrentUser(request));
    }
}
