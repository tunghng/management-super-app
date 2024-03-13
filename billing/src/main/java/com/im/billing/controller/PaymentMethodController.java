package com.im.billing.controller;

import com.im.billing.dto.model.PaymentMethodDto;
import com.im.billing.dto.response.page.PageData;
import com.im.billing.dto.response.page.PageLink;
import com.im.billing.service.PaymentMethodService;
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
@RequestMapping("/api/billing/method")
public class PaymentMethodController extends BaseController {
    @Autowired
    PaymentMethodService paymentMethodService;

    @GetMapping
    @Operation(summary = "Get payment methods (getPaymentMethods)",
            description = "Retrieve a list of payment methods with optional filtering and pagination.")
    public PageData<PaymentMethodDto> getPaymentMethods(
            @Parameter(description = "Page number for pagination")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page for pagination")
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "Search text for filtering payment methods by name")
            @RequestParam(required = false) String searchText,
            @Parameter(description = "Search Match Case Or Not")
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText, null, null);
        return paymentMethodService.findPaymentMethods(pageLink, getCurrentUser(request).getTenantId(), isSearchMatchCase);
    }

    @GetMapping("{methodId}")
    @Operation(summary = "Get payment method (getPaymentMethod)",
            description = "Retrieve a payment method by its ID.")
    public PaymentMethodDto getPaymentMethod(
            @PathVariable("methodId") UUID paymentMethodId,
            HttpServletRequest request
    ) {
        return paymentMethodService.findByIdAndTenantId(paymentMethodId, getCurrentUser(request));
    }

    @PostMapping
    @Operation(summary = "Create or update payment method (savePaymentMethod)",
            description = "Create or update payment method. For creating payment method, remove the id field.")
    public PaymentMethodDto savePaymentMethod(
            @Valid @RequestBody PaymentMethodDto paymentMethodDto,
            HttpServletRequest request
    ) {
        return paymentMethodService.save(paymentMethodDto, getCurrentUser(request));
    }
}
