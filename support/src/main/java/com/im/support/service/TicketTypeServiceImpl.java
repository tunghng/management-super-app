package com.im.support.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.support.dto.mapper.TicketTypeMapper;
import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.LogDto;
import com.im.support.dto.model.TicketTypeDto;
import com.im.support.dto.response.page.PageData;
import com.im.support.dto.response.page.PageLink;
import com.im.support.exception.BadRequestException;
import com.im.support.model.TicketType;
import com.im.support.model.enums.ActionStatus;
import com.im.support.model.enums.ActionType;
import com.im.support.model.enums.EntityType;
import com.im.support.repository.TicketTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class TicketTypeServiceImpl implements TicketTypeService {

    private final static String TYPE_NAME_ALREADY_EXIST = "Ticket type with name [%s] is already exist";

    @Autowired
    TicketTypeRepository ticketTypeRepository;

    @Autowired
    LogService logService;

    @Autowired
    TicketTypeMapper ticketTypeMapper;

    @Override
    public PageData<TicketTypeDto> findTicketTypes(PageLink pageLink, UUID tenantId) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());
        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");
        Page<TicketType> ticketTypes = ticketTypeRepository.findTicketTypes(
                searchText,
                tenantId,
                pageable
        );
        Page<TicketTypeDto> ticketTypeDtoList = ticketTypes.map(ticketTypeMapper::toDto);
        return new PageData<TicketTypeDto>(ticketTypeDtoList);
    }

    @Override
    public TicketTypeDto findByNameAndTenantId(String name, UUID tenantId) {
        return ticketTypeMapper.toDto(ticketTypeRepository.findByNameAndTenantId(name, tenantId));
    }

    @Override
    public TicketTypeDto save(TicketTypeDto ticketTypeDto, AppUserDto currentUser) {

        TicketType type = ticketTypeDto.getId() != null
                ? ticketTypeMapper.toModel(ticketTypeDto) : new TicketType();

        checkIfNameExist(ticketTypeDto.getName(), currentUser);

        boolean isUpdating = ticketTypeDto.getId() != null;

        BeanUtils.copyProperties(ticketTypeDto, type);

        type.setTenantId(currentUser.getTenantId());
        TicketType savedType = ticketTypeRepository.save(type);
        ObjectMapper objectMapper = new ObjectMapper();
        logService.save(LogDto.builder()
                .entityType(EntityType.TICKET_TYPE)
                .entityId(savedType.getId()).actionStatus(ActionStatus.FAILURE)
                .actionType(isUpdating ? ActionType.UPDATED : ActionType.CREATED)
                .actionStatus(ActionStatus.SUCCESS)
                .actionData(objectMapper.valueToTree(ticketTypeDto))
                .build(), currentUser);
        return ticketTypeMapper.toDto(savedType);
    }

    @Override
    public TicketType toModel(String type) {
        return ticketTypeRepository.findByName(type);
    }

    @Override
    public String toString(TicketTypeDto type) {
        return type != null ? type.getName() : null;
    }

    private void checkIfNameExist(String name, AppUserDto currentUser) {
        TicketType type = ticketTypeRepository.findByNameAndTenantId(name, currentUser.getTenantId());
        if (type != null) {
            logService.save(LogDto.builder()
                    .entityType(EntityType.TICKET_TYPE)
                    .actionStatus(ActionStatus.FAILURE)
                    .actionType(ActionType.CREATED)
                    .actionFailureDetails(String.format("Ticket type with name [%s] is already exist", name))
                    .build(), currentUser);
            throw new BadRequestException(
                    String.format(TYPE_NAME_ALREADY_EXIST, name)
            );
        }
    }
}

