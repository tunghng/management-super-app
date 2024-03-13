package com.im.sso.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.sso.dto.mapper.WhiteLabelMapper;
import com.im.sso.dto.model.AppUserDto;
import com.im.sso.dto.model.LogDto;
import com.im.sso.dto.model.WhiteLabelDto;
import com.im.sso.model.WhiteLabel;
import com.im.sso.model.enums.ActionStatus;
import com.im.sso.model.enums.ActionType;
import com.im.sso.model.enums.EntityType;
import com.im.sso.repository.WhiteLabelRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WhiteLabelServiceImpl implements WhiteLabelService {

    @Autowired
    WhiteLabelRepository whiteLabelRepository;

    @Autowired
    WhiteLabelMapper whiteLabelMapper;

    @Autowired
    LogService logService;

    @Override
    public WhiteLabelDto findByTenantId(UUID tenantId) {
        WhiteLabel whiteLabel = whiteLabelRepository.findByTenantId(tenantId)
                .orElse(new WhiteLabel());
        return whiteLabelMapper.toDto(whiteLabel);
    }

    @Override
    public WhiteLabelDto save(WhiteLabelDto whiteLabelDto, AppUserDto currentUser) {
        WhiteLabel whiteLabel = whiteLabelRepository.findByTenantId(currentUser.getTenantId())
                .orElse(new WhiteLabel());
        boolean isAdded = whiteLabel.getId() == null;
        BeanUtils.copyProperties(whiteLabelDto, whiteLabel, "id", "createdAt", "updatedAt");
        whiteLabel.setTenantId(currentUser.getTenantId());
        whiteLabel.setCreatedBy(currentUser.getTenantId());
        whiteLabel.setUpdatedBy(currentUser.getTenantId());
        WhiteLabel savedWhiteLabel = whiteLabelRepository.saveAndFlush(whiteLabel);
        ObjectMapper objectMapper = new ObjectMapper();
        logService.save(LogDto.builder()
                .entityType(EntityType.WHITE_LABEL)
                .entityId(savedWhiteLabel.getId())
                .actionStatus(ActionStatus.SUCCESS)
                .actionData(objectMapper.valueToTree(whiteLabelDto))
                .actionType(isAdded ? ActionType.CREATED : ActionType.UPDATED)
                .build(), currentUser);
        return whiteLabelMapper.toDto(savedWhiteLabel);
    }
}
