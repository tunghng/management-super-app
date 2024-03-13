package com.im.support.config;

import com.im.support.model.AppUser;
import com.im.support.model.Ticket;
import com.im.support.model.TicketUser;
import com.im.support.model.enums.RoleType;
import com.im.support.repository.AppUserRepository;
import com.im.support.repository.TicketRepository;
import com.im.support.repository.TicketUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class LoadDataConfig {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketUserRepository ticketUserRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            if (ticketUserRepository.count() == 0) {
                List<Ticket> tickets = ticketRepository.findAll();
                for (Ticket ticket : tickets) {
                    Collection<UUID> toUserIds = new ArrayList<>();
                    toUserIds.addAll(appUserRepository.findByTenantIdAndContactId(
                            ticket.getTenantId(),
                            ticket.getContact().getId()
                    ).stream().map(AppUser::getId).collect(Collectors.toList()));
                    toUserIds.addAll(appUserRepository.findByRoleAndTenantId(
                            RoleType.TENANT.name(),
                            ticket.getTenantId()
                    ).stream().map(AppUser::getId).collect(Collectors.toList()));
                    toUserIds.addAll(appUserRepository.findByRoleAndTenantId(
                            RoleType.MANAGER.name(),
                            ticket.getTenantId()
                    ).stream().map(AppUser::getId).collect(Collectors.toList()));
                    for (UUID userId : toUserIds) {
                        ticketUserRepository.save(TicketUser.builder()
                                .ticket(ticket)
                                .userId(userId)
                                .isRead(false)
                                .build());
                    }
                }
            }
        };
    }

}
