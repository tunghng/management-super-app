package com.im.sso.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.sso.dto.mapper.DataKvMapper;
import com.im.sso.dto.model.AppUserDto;
import com.im.sso.dto.model.DataKvDto;
import com.im.sso.dto.model.LogDto;
import com.im.sso.model.DataKv;
import com.im.sso.model.enums.ActionStatus;
import com.im.sso.model.enums.ActionType;
import com.im.sso.model.enums.AppInfoType;
import com.im.sso.model.enums.EntityType;
import com.im.sso.repository.DataKvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppInfoServiceImpl implements AppInfoService {

    private final DataKvRepository dataKvRepository;

    private final DataKvMapper dataKvMapper;

    private final LogService logService;

    @Override
    public List<DataKvDto> findInfo() {
        DataKv appVersion = dataKvRepository.findById(AppInfoType.APP_VERSION.name()).orElse(null);
        DataKv appHotline = dataKvRepository.findById(AppInfoType.APP_HOTLINE.name()).orElse(null);
        DataKv hostname = dataKvRepository.findById(AppInfoType.HOSTNAME.name()).orElse(null);
        DataKv signUpAvailable = dataKvRepository.findById(AppInfoType.SIGNUP_AVAILABLE.name()).orElse(null);
        DataKv defaultTenantEmail = dataKvRepository.findById(AppInfoType.DEFAULT_TENANT_EMAIL.name()).orElse(null);
        DataKv defaultContactId = dataKvRepository.findById(AppInfoType.DEFAULT_CONTACT_ID.name()).orElse(null);
        assert appVersion != null;
        assert appHotline != null;
        assert hostname != null;
        assert signUpAvailable != null;
        assert defaultTenantEmail != null;
        assert defaultContactId != null;
        return dataKvMapper.toDtoList(List.of(appVersion, appHotline, hostname,
                signUpAvailable, defaultTenantEmail, defaultContactId));
    }

    @Override
    public List<DataKvDto> save(List<DataKvDto> appInfoList, AppUserDto currentUser) {
        appInfoList.forEach(
                info -> {
                    AppInfoType.lookup(info.getKey());
                    DataKv newData = dataKvMapper.toModel(info);
                    dataKvRepository.save(newData);
                }
        );
        ObjectMapper objectMapper = new ObjectMapper();
        logService.save(LogDto.builder()
                .entityType(EntityType.DATA_KV)
                .actionData(objectMapper.valueToTree(appInfoList))
                .actionType(ActionType.CREATED)
                .actionStatus(ActionStatus.SUCCESS)
                .build(), currentUser);
        return findInfo();
    }
}
