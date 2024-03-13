package com.im.billing.controller;

import com.im.billing.dto.model.AppUserDto;
import com.im.billing.dto.model.BillDto;
import com.im.billing.dto.model.BillSaveDto;
import com.im.billing.dto.response.Response;
import com.im.billing.dto.response.page.PageData;
import com.im.billing.dto.response.page.PageLink;
import com.im.billing.model.enums.BillState;
import com.im.billing.model.enums.RoleType;
import com.im.billing.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/billing")
public class BillController extends BaseController {
    private final List<String> BILL_KEYS = List.of(
            "code", "title", "type", "price", "state", "contactName",
            "createdAt", "dueDate", "closedAt", "closedBy"
    );

    @Autowired
    BillService billService;

    @GetMapping
    @Operation(summary = "Get Bills (getBills)",
            description = "Retrieve a list of bills with optional filtering and pagination.")
    public PageData<BillDto> getBills(
            @Parameter(description = "Page number for pagination")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page for pagination")
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "Search columns: title, code, typeName")
            @RequestParam(required = false) String searchText,
            @Parameter(description = "State of the bill")
            @RequestParam(required = false) List<BillState> state,
            @Parameter(description = "List of BillType Ids")
            @RequestParam(required = false) List<UUID> typeId,
            @Parameter(description = "List of Contact Ids")
            @RequestParam(required = false) List<UUID> contactId,
            @Parameter(description = "A boolean value representing the bill isDeleted flag.")
            @RequestParam(required = false) Boolean isDeleted,
            @Parameter(description = "Sort order, can be 'asc' or 'desc'")
            @RequestParam(required = false) String sortProperty,
            @Parameter(description = "Name of the property to sort by")
            @RequestParam(required = false) String sortOrder,
            @Parameter(description = "Filter column: createdAt. `endTs` is required.")
            @RequestParam(required = false) Long createdAtStartTs,
            @Parameter(description = "Filter column: createdAt. `startTs` is required.")
            @RequestParam(required = false) Long createdAtEndTs,
            @Parameter(description = "Filter column: dueDate. `endTs` is required.")
            @RequestParam(required = false) Long dueDateStartTs,
            @Parameter(description = "Filter column: dueDate. `startTs` is required.")
            @RequestParam(required = false) Long dueDateEndTs,
            @Parameter(description = "Filter column: updatedAt. `endTs` is required.")
            @RequestParam(required = false) Long updatedAtStartTs,
            @Parameter(description = "Filter column: updatedAt. `startTs` is required.")
            @RequestParam(required = false) Long updatedAtEndTs,
            @Parameter(description = "Search Match Case Or Not")
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,

            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText, sortProperty, sortOrder);
        AppUserDto currentUser = getCurrentUser(request);
        return billService.findBills(
                pageLink,
                state,
                typeId,
                createdAtStartTs,
                createdAtEndTs,
                dueDateStartTs,
                dueDateEndTs,
                updatedAtStartTs,
                updatedAtEndTs,
                currentUser,
                contactId,
                isDeleted,
                isSearchMatchCase
        );
    }

    @GetMapping("export")
    @Operation(summary = "Export bills (exportToExcel)",
            description = "Support the following attributes: code, title, contactName, description, state, type, createdAt, closedAt, closedBy, reviewTitle, reviewBody")
    public ResponseEntity<Object> exportToExcel(
            @Parameter(description = "List of attributes to export, if not provided, export all attributes. " +
                    "Available attributes: code, title, type, price, state, contactName, createdAt, dueDate, closedAt, closedBy"
            )
            @RequestParam(value = "attributes", required = false) String[] attributes,
            @Parameter(description = "Name of the exported file, if not provided, " +
                    "the file name will be in the pattern Bills_yyyy_MM_dd_HH_mm_ss.xlsx"
            )
            @RequestParam(value = "fileName", required = false) String fileName,
            @Parameter(description = "The language for report's headers in Excel, if not provided, it will be translated into Vietnamese. ",
                    schema = @Schema(type = "string", allowableValues = {"vi-VN", "en-US"})
            )
            @RequestHeader(value = "Content-Language", required = false) String contentLanguage,
            @Parameter(description = "State of the bill")
            @RequestParam(required = false) BillState state,
            @Parameter(description = "Contact Id")
            @RequestParam(value = "contactId", required = false) UUID fromContactId,
            @Parameter(description = "Bill Type Id, see the bill-type-controller")
            @RequestParam(required = false) UUID typeId,
            @Parameter(description = "Start timestamp for filtering bills in milliseconds")
            @RequestParam(value = "createdAtStartTs", required = false) Long createdAtStartTs,
            @Parameter(description = "End timestamp for filtering bills in milliseconds")
            @RequestParam(value = "createdAtEndTs", required = false) Long createdAtEndTs,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        UUID contactId = currentUser.getRole().equals(RoleType.CUSTOMER.toString())
                ? currentUser.getContactId() : null;
        if (contentLanguage == null) {
            contentLanguage = "vi-VN";
        }
        List<String> attributeList = (attributes == null) ? BILL_KEYS : Arrays.asList(attributes);
        return billService.exportData(attributeList, fileName, contentLanguage, createdAtStartTs, createdAtEndTs,
                currentUser, contactId, state, typeId, fromContactId);
    }

    @GetMapping("{billId}")
    @Operation(summary = "Get bill by Id",
            description = "Retrieve a bill by its ID.")
    public BillDto getBillById(
            @Parameter(description = "ID of the bill to retrieve.")
            @PathVariable("billId") UUID billId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return billService.findDetailById(billId, currentUser);
    }

    @PutMapping("{billId}/closed")
    @Operation(summary = "Close bill (closeBill)",
            description = "Close a bill by its ID.")
    public BillDto closeBill(
            @Parameter(description = "ID of the bill to close")
            @PathVariable("billId") UUID billId,
            HttpServletRequest request
    ) {
        return billService.closeBill(billId, getCurrentUser(request));
    }

    @PutMapping("{billId}/paid")
    @Operation(summary = "Pay bill (payBill)",
            description = "Pay a bill by its ID.")
    public BillDto payBill(
            @Parameter(description = "ID of the bill to pay")
            @PathVariable("billId") UUID billId,
            HttpServletRequest request
    ) {
        return billService.payBill(billId, getCurrentUser(request));
    }

    @DeleteMapping("{billId}")
    @Transactional
    @Operation(summary = "Delete bill by Id (deleteById)")
    public ResponseEntity<Response> deleteById(
            @Parameter(description = "ID of the bill to delete")
            @PathVariable("billId") UUID billId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        billService.delete(billId, currentUser);
        return ResponseEntity.ok(
                new Response(String.format("Bill with id [%s] move to trash bin", billId))
        );
    }

    @PutMapping("{billId}/restore")
    @Transactional
    @Operation(summary = "Restore bill By Id (restoreBillById)")
    public ResponseEntity<Response> restoreBillById(
            @Parameter(description = "ID of the bill to restore")
            @PathVariable UUID billId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        billService.restore(billId, currentUser);
        return ResponseEntity.ok(
                new Response(String.format("Bill with id [%s] restore from trash bin", billId))
        );
    }

    @PostMapping
    @Operation(summary = "Create or update bill (saveBill)",
            description = "Create or update bill. For creating bill, remove the id field.")
    public BillDto saveBill(
            @Parameter(description = "Bill object to create or update")
            @Valid @RequestBody BillSaveDto billSaveDto,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return billService.save(billSaveDto, currentUser);
    }
}
