package com.im.billing.service;

import com.im.billing.dto.mapper.BillTypeMapper;
import com.im.billing.dto.model.AppUserDto;
import com.im.billing.dto.model.BillTypeDto;
import com.im.billing.dto.response.page.PageData;
import com.im.billing.dto.response.page.PageLink;
import com.im.billing.exception.BadRequestException;
import com.im.billing.exception.UnAuthorizedException;
import com.im.billing.model.BillType;
import com.im.billing.model.enums.RoleType;
import com.im.billing.repository.BillTypeRepository;
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
public class BillTypeServiceImpl implements BillTypeService {

    private final static String TYPE_NAME_ALREADY_EXIST = "Bill type with name [%s] is already exist";

    @Autowired
    BillTypeRepository billTypeRepository;
    @Autowired
    BillTypeMapper billTypeMapper;

    @Override
    public PageData<BillTypeDto> findBillTypes(PageLink pageLink, AppUserDto currentUser, Boolean isSearchMatchCase) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());
        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<BillType> billTypes = billTypeRepository.findBillTypes(
                searchText,
                isSearchMatchCase,
                currentUser.getTenantId(),
                pageable
        );
        Page<BillTypeDto> billTypeDtoList = billTypes.map(billTypeMapper::toDto);
        return new PageData<>(billTypeDtoList);
    }

    @Override
    public BillTypeDto findByNameAndTenantId(String name, AppUserDto currentUser) {
        BillType billType = billTypeRepository.findByNameAndTenantId(name, currentUser.getTenantId()).orElse(null);
        return billType != null ? billTypeMapper.toDto(billType) : null;
    }

    @Override
    public BillTypeDto findById(UUID billTypeId, AppUserDto currentUser) {
        BillType billType = billTypeRepository.findByIdAndTenantId(billTypeId, currentUser.getTenantId()).orElseThrow(
                () -> new BadRequestException(String.format("Bill type with id [%s] is not found", billTypeId))
        );
        return billTypeMapper.toDto(billType);
    }

    @Override
    public BillTypeDto save(BillTypeDto billTypeDto, AppUserDto currentUser) {
        isTenantAdmin(currentUser);
        UUID tenantId = currentUser.getTenantId();

        BillType billType = billTypeDto.getId() != null
                ? billTypeMapper.toModel(billTypeDto)
                : new BillType();

        checkIfNameExist(billTypeDto.getName(), tenantId);

        BeanUtils.copyProperties(billTypeDto, billType);
        billType.setTenantId(tenantId);
        BillType savedType = billTypeRepository.save(billType);
        return billTypeMapper.toDto(savedType);
    }

    @Override
    public BillType toBillType(String type) {
        return billTypeRepository.findByName(type);
    }

    @Override
    public String toString(BillType billType) {
        return billType.getName();
    }

    private void isTenantAdmin(AppUserDto currentUser) {
        if (!currentUser.getRole().equals(RoleType.TENANT.toString()))
            throw new UnAuthorizedException("You does not have permission to do this action");
    }

    private void checkIfNameExist(String name, UUID tenantId) {
        BillType type = billTypeRepository.findByNameAndTenantId(name, tenantId).orElse(null);
        if (type != null) {
            throw new BadRequestException(
                    String.format(TYPE_NAME_ALREADY_EXIST, name)
            );
        }
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD); Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
