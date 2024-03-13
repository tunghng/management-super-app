package com.im.support.model;

import com.im.support.model.enums.PriorityType;
import com.im.support.model.enums.TicketState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket extends BaseEntity implements HasTenantId {

    @OneToMany(mappedBy = "ticket")
    Collection<TicketFeedback> feedbacks;

    @OneToOne(mappedBy = "review")
    TicketReview review;

    @Column(name = "ticket_code")
    private String code;

    private String title;

    @Column(length = 10485760)
    private String description;

    @Enumerated(EnumType.STRING)
    private PriorityType priority;

    @Enumerated(EnumType.STRING)
    private TicketState state;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_type_id")
    private TicketType type;

    private LocalDateTime closedAt;

    private UUID tenantId;

    @Column(
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT FALSE"
    )
    private Boolean isDeleted = false;

    @ElementCollection(targetClass = String.class)
    private Collection<String> attachedFile;
}
