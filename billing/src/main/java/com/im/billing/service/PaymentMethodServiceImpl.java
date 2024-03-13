package com.im.billing.service;

import com.im.billing.dto.mapper.PaymentMethodMapper;
import com.im.billing.dto.model.AppUserDto;
import com.im.billing.dto.model.PaymentMethodDto;
import com.im.billing.dto.response.page.PageData;
import com.im.billing.dto.response.page.PageLink;
import com.im.billing.exception.BadRequestException;
import com.im.billing.exception.UnAuthorizedException;
import com.im.billing.model.PaymentMethod;
import com.im.billing.model.enums.RoleType;
import com.im.billing.repository.PaymentMethodRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Autowired
    PaymentMethodMapper paymentMethodMapper;

    @Override
    public PageData<PaymentMethodDto> findPaymentMethods(PageLink pageLink, UUID tenantId, Boolean isSearchMatchCase) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());
        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<PaymentMethod> paymentMethods = paymentMethodRepository.findPaymentMethods(
                searchText,
                isSearchMatchCase,
                tenantId,
                pageable
        );
        Page<PaymentMethodDto> paymentMethodDtoList = paymentMethods.map(paymentMethodMapper::toDto);
        return new PageData<>(paymentMethodDtoList);
    }

    @Override
    public PaymentMethodDto findByNameAndTenantId(String name, UUID tenantId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByNameAndTenantId(name, tenantId).orElse(null);
        return paymentMethod != null ? paymentMethodMapper.toDto(paymentMethod) : null;
    }

    @Override
    public PaymentMethodDto findByIdAndTenantId(UUID paymentMethodId, AppUserDto currentUser) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByIdAndTenantId(paymentMethodId, currentUser.getTenantId()).orElseThrow(
                () -> new BadRequestException(String.format("Payment method with id [%s] is not found", paymentMethodId))
        );
        return paymentMethodMapper.toDto(paymentMethod);
    }

    @Override
    public PaymentMethodDto save(PaymentMethodDto paymentMethodDto, AppUserDto currentUser) {
        isTenantAdmin(currentUser);
        UUID tenantId = currentUser.getTenantId();
        PaymentMethod paymentMethod;
        if (paymentMethodDto.getId() != null) {
            paymentMethod = paymentMethodRepository.findById(paymentMethodDto.getId()).orElse(null);
        } else {
            paymentMethod = paymentMethodRepository.findByNameAndTenantId(paymentMethodDto.getName(), tenantId).orElse(null);
            if (paymentMethod != null)
                throw new BadRequestException("Payment method with name [" + paymentMethod.getName() + "] is already exist");
            paymentMethod = new PaymentMethod();
        }
        BeanUtils.copyProperties(paymentMethodDto, paymentMethod);
        paymentMethod.setTenantId(tenantId);
        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        return paymentMethodMapper.toDto(savedPaymentMethod);
    }

    private void isTenantAdmin(AppUserDto currentUser) {
        if (!currentUser.getRole().equals(RoleType.TENANT.toString()))
            throw new UnAuthorizedException("You does not have permission to do this action");
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD); Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
