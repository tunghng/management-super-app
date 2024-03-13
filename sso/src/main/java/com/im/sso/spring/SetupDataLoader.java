package com.im.sso.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.im.sso.dto.mapper.AppUserMapper;
import com.im.sso.dto.model.AppUserDto;
import com.im.sso.model.AccountPlan;
import com.im.sso.model.AppComponent;
import com.im.sso.model.DataKv;
import com.im.sso.model.UserComponent;
import com.im.sso.model.enums.AccountPlanType;
import com.im.sso.model.enums.AppInfoType;
import com.im.sso.model.enums.AuthorityType;
import com.im.sso.model.enums.RoleType;
import com.im.sso.repository.AccountPlanRepository;
import com.im.sso.repository.AppComponentRepository;
import com.im.sso.repository.DataKvRepository;
import com.im.sso.repository.UserComponentRepository;
import com.im.sso.service.UserCredentialsServiceImpl;
import com.im.sso.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final boolean alreadySetup = false;
    private final AccountPlanRepository accountPlanRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserCredentialsServiceImpl userCredentialsService;
    @Autowired
    AppComponentRepository appComponentRepository;
    @Autowired
    UserComponentRepository userComponentRepository;
    @Autowired
    DataKvRepository dataKvRepository;
    @Autowired
    AppUserMapper appUserMapper;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        createAppInfoIfNotFound(AppInfoType.APP_VERSION.name(), "0.1.0");
        createAppInfoIfNotFound(AppInfoType.APP_HOTLINE.name(), "0374171617");
        createAppInfoIfNotFound(AppInfoType.HOSTNAME.name(), "https://industrial.innovation.com.vn/");
        createAppInfoIfNotFound(AppInfoType.SIGNUP_AVAILABLE.name(), "false");
        createAppInfoIfNotFound(AppInfoType.DEFAULT_TENANT_EMAIL.name(), "tenant@tma.com.vn");
        createAppInfoIfNotFound(AppInfoType.DEFAULT_CONTACT_ID.name(), "1f9d7728-a826-4446-8e2e-e4e938c9bd34");

        createAccountPlanIfNotFound(AccountPlanType.BASIC);
        createAccountPlanIfNotFound(AccountPlanType.OPERATION);
        createAccountPlanIfNotFound(AccountPlanType.DIGITAL);

        final AppComponent contactComponent = createComponentIfNotFound("contact", "/api/contact");
//        final AppComponent projectComponent = createComponentIfNotFound("project", "/api/project");
        deleteComponentIfFound("project");
        deleteComponentIfFound("notification");
        final AppComponent newsComponent = createComponentIfNotFound("news", "/api/news");
        final AppComponent accountComponent = createComponentIfNotFound("account", "/api/user");
        final AppComponent documentComponent = createComponentIfNotFound("document", "/api/document");
        final AppComponent supportComponent = createComponentIfNotFound("support", "/api/support");
        final AppComponent announcementComponent = createComponentIfNotFound("announcement", "/api/announcement");
        final AppComponent billingComponent = createComponentIfNotFound("billing", "/api/billing");
        final AppComponent formComponent = createComponentIfNotFound("form", "/api/form");
        final AppComponent monitorComponent = createComponentIfNotFound("monitor", "/api/monitor");
        final AppComponent cameraComponent = createComponentIfNotFound("camera", "/api/camera");
        final AppComponent deviceComponent = createComponentIfNotFound("device", "/api/device");
        final AppComponent parkingLotComponent = createComponentIfNotFound("parking-lot", "/api/parking-lot");
        final AppComponent maintenanceComponent = createComponentIfNotFound("maintenance", "/api/maintenance");
        final AppComponent accessControlComponent = createComponentIfNotFound("access-control", "/api/access-control");
        final AppComponent trafficLightComponent = createComponentIfNotFound("traffic-light", "/api/traffic-light");

        final Collection<AppComponent> managerComponentList = new ArrayList<>(Arrays.asList(formComponent,
                documentComponent, newsComponent, supportComponent, announcementComponent));
        final Collection<AppComponent> userComponentList1 = new ArrayList<>(Arrays.asList(formComponent,
                documentComponent, newsComponent));
        final Collection<AppComponent> userComponentList2 = new ArrayList<>(Collections.singletonList(newsComponent));

        try {
            createUserIfNotFound("System", "Admin", "sysadmin@tma.com.vn",
                    "0905040302", AuthorityType.SYS_ADMIN,
                    RoleType.SYS_ADMIN, null, null);

            createUserIfNotFound("Tenant", "Admin", "tenant@tma.com.vn",
                    "0905040302", AuthorityType.TENANT_ADMIN,
                    RoleType.TENANT, null, null);

            AppUserDto tenant = userService.findByEmail("tenant@tma.com.vn");

            createUserIfNotFound("Manager ", "User",
                    "manager@tma.com.vn", "0905040302",
                    AuthorityType.CUSTOMER_USER, RoleType.MANAGER,
                    tenant.getId(), managerComponentList);
            createUserIfNotFound("Customer 1", "User",
                    "customer1@tma.com.vn", "0905040302",
                    AuthorityType.CUSTOMER_USER, RoleType.CUSTOMER,
                    tenant.getId(), userComponentList1);
            createUserIfNotFound("Customer 2", "User",
                    "customer2@tma.com.vn", "0905040302",
                    AuthorityType.CUSTOMER_USER, RoleType.CUSTOMER,
                    tenant.getId(), userComponentList2);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    void createAccountPlanIfNotFound(final AccountPlanType planType) {
        AccountPlan accountPlan = accountPlanRepository.findByName(planType).orElse(null);
        if (accountPlan == null) {
            accountPlan = new AccountPlan();
            accountPlan.setName(planType);
            accountPlanRepository.save(accountPlan);
        }
    }

    @Transactional
    AppComponent createComponentIfNotFound(final String name, String urlBase) {
        AppComponent userComponent = appComponentRepository.findByName(name);
        if (userComponent == null) {
            userComponent = new AppComponent();
            userComponent.setName(name);
            userComponent.setUrlBase(urlBase);
            userComponent = appComponentRepository.save(userComponent);
        }
        return userComponent;
    }

    @Transactional
    void deleteComponentIfFound(final String name) {
        AppComponent appComponent = appComponentRepository.findByName(name);
        if (appComponent != null) {
            userComponentRepository.findByAppComponent(appComponent)
                    .forEach(userComponent -> {
                        userComponentRepository.delete(userComponent);
                    });
            appComponentRepository.delete(appComponent);
        }
    }

    @Transactional
    void createUserIfNotFound(final String firstName, final String lastName,
                              final String email, final String phone,
                              final AuthorityType authority, final RoleType role,
                              UUID tenantId, final Collection<AppComponent> components)
            throws JsonProcessingException {
        AppUserDto user = userService.findByEmail(email);
        if (user == null) {
            AppUserDto userDto = new AppUserDto();
            userDto.setFirstName(firstName);
            userDto.setLastName(lastName);
            userDto.setEmail(email);
            userDto.setPhone(phone);
            userDto.setAuthority(authority.name());
            userDto.setRole(role.name());

            if (!AuthorityType.SYS_ADMIN.equals(authority)) {
                userDto.setTenantId(tenantId);
            }
            AppUserDto savedUser = userService.save(userDto, null);
            if (AuthorityType.CUSTOMER_USER.equals(authority)) {
                components.forEach(component -> {
                    UserComponent userComponent = new UserComponent();
                    userComponent.setUser(appUserMapper.toModel(savedUser));
                    userComponent.setAppComponent(component);
                    List<String> permissions = List.of("READ", "ADD", "EDIT");
                    userComponent.setPermissions(permissions);
                    userComponentRepository.save(userComponent);
                });
            }
            userCredentialsService.setPassword(savedUser.getId());
        }
    }

    @Transactional
    DataKv createAppInfoIfNotFound(final String key, String value) {
        DataKv dataKv = dataKvRepository.findById(key).orElse(null);
        if (dataKv == null) {
            dataKv = new DataKv();
            dataKv.setKey(key);
            dataKv.setValue(value);
            dataKv = dataKvRepository.save(dataKv);
        }
        return dataKv;
    }
}
