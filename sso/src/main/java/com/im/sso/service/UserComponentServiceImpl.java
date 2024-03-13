package com.im.sso.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.sso.dto.mapper.AppComponentMapper;
import com.im.sso.dto.mapper.UserComponentMapper;
import com.im.sso.dto.model.AppComponentDto;
import com.im.sso.dto.model.AppUserDto;
import com.im.sso.dto.model.LogDto;
import com.im.sso.dto.model.UserComponentDto;
import com.im.sso.dto.request.UserComponentPermissionRequest;
import com.im.sso.dto.request.UserComponentRequest;
import com.im.sso.exception.BadRequestException;
import com.im.sso.model.AppComponent;
import com.im.sso.model.AppUser;
import com.im.sso.model.UserComponent;
import com.im.sso.model.enums.ActionStatus;
import com.im.sso.model.enums.ActionType;
import com.im.sso.model.enums.EntityType;
import com.im.sso.repository.AppComponentRepository;
import com.im.sso.repository.AppUserRepository;
import com.im.sso.repository.UserComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserComponentServiceImpl implements UserComponentService {

    private final AppUserRepository appUserRepository;

    private final AppComponentRepository appComponentRepository;

    private final UserComponentRepository userComponentRepository;

    private final AppComponentMapper appComponentMapper;

    private final UserComponentMapper userComponentMapper;

    private final LogService logService;

    @Override
    public Collection<AppComponentDto> findAllTenantComponent() {
        List<AppComponent> appComponentList = appComponentRepository.findAll();
        Collection<AppComponentDto> appComponentDtoList = new ArrayList<>();
        appComponentList.forEach(appComponent -> {
            AppComponentDto dto = appComponentMapper.toDto(appComponent);
            dto.setPermissions(List.of("ALL"));
            appComponentDtoList.add(dto);
        });
        return appComponentDtoList;
    }

    @Override
    public Collection<AppComponentDto> findAllCustomerComponent(UUID userId) {
        List<UserComponent> userComponentList = userComponentRepository.findAllByUserId(userId);
        Collection<AppComponentDto> appComponentDtoList = new ArrayList<>();
        userComponentList.forEach(userComponent -> {
            AppComponentDto dto = appComponentMapper.toDto(userComponent.getAppComponent());
            dto.setPermissions(userComponent.getPermissions());
            appComponentDtoList.add(dto);
        });
        return appComponentDtoList;
    }

    @Override
    public UserComponentDto save(AppUserDto userDto, AppComponentDto componentDto, List<String> permissions, AppUserDto currentUser) {
        UserComponent existComponent = userComponentRepository.findByAppComponentIdAndUserId(
                componentDto.getId(), userDto.getId());
        AppUser user = appUserRepository.findById(userDto.getId()).get();
        AppComponent component = appComponentRepository.findByName(componentDto.getName());
        UserComponent userComponent = existComponent != null ? existComponent
                : new UserComponent(user, component, permissions);
        List<String> newPermissions = new ArrayList<>(userComponent.getPermissions());
        newPermissions.removeAll(userComponent.getPermissions());
        newPermissions.addAll(permissions);
        userComponent.setPermissions(newPermissions);
        UserComponent savedUserComponent = userComponentRepository.saveAndFlush(userComponent);
        ObjectMapper objectMapper = new ObjectMapper();
        logService.save(LogDto.builder()
                .entityType(EntityType.USER_COMPONENT)
                .entityId(component.getId())
                .actionType(ActionType.ASSIGNED_TO_USER)
                .actionStatus(ActionStatus.SUCCESS)
                .actionData(objectMapper.valueToTree(
                        new UserComponentPermissionRequest(
                                userDto.getId(),
                                componentDto.getName(),
                                permissions
                        )
                )).build(), currentUser);
        return userComponentMapper.toDto(savedUserComponent);
    }

    @Override
    public String delete(AppUserDto userDto, AppComponentDto componentDto, AppUserDto currentUser) {
        UserComponent existComponent = userComponentRepository.findByAppComponentIdAndUserId(componentDto.getId(), userDto.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        if (existComponent == null) {
            logService.save(LogDto.builder()
                    .entityType(EntityType.USER_COMPONENT)
                    .entityId(componentDto.getId())
                    .actionType(ActionType.UNASSIGNED_FROM_USER)
                    .actionStatus(ActionStatus.FAILURE)
                    .actionData(objectMapper.valueToTree(
                                    new UserComponentRequest(
                                            userDto.getId(),
                                            componentDto.getName())
                            )
                    )
                    .actionFailureDetails(String.format("User with id [%s] is not assigned to component [%s]",
                            userDto.getId(), componentDto.getName()))
                    .build(), currentUser);
            throw new BadRequestException(String.format("User with id [%s] is not assigned to component [%s]",
                    userDto.getId(), componentDto.getName()));
        }
        userComponentRepository.delete(existComponent);
        logService.save(LogDto.builder()
                .entityType(EntityType.USER_COMPONENT)
                .entityId(componentDto.getId())
                .actionType(ActionType.UNASSIGNED_FROM_USER)
                .actionStatus(ActionStatus.SUCCESS)
                .actionData(objectMapper.valueToTree(
                        new UserComponentRequest(
                                userDto.getId(),
                                componentDto.getName()
                        )
                ))
                .build(), currentUser);
        return String.format("User with id [%s] is unassigned from component [%s] successfully.",
                userDto.getId(), componentDto.getName());
    }

    @Override
    public Collection<AppComponentDto> convertUserComponentToComponent(Collection<UserComponent> userComponents) {
        return userComponents.stream().map(userComponent ->
                        appComponentMapper.toDto(userComponent.getAppComponent()))
                .collect(Collectors.toList());
    }
}
