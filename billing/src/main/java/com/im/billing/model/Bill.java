package com.im.billing.model;

import com.im.billing.model.enums.BillState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bill extends BaseEntity implements HasTenantId {
    @Column(name = "bill_code")
    private String code;
    private String title;
    @Column(length = 10485760)
    private String description;
    private long price;
    private UUID tenantId;
    private UUID paidBy;
    @Enumerated(EnumType.STRING)
    private BillState state;
    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_type_id")
    private BillType type;
    private LocalDateTime closedAt;
    private LocalDateTime dueDate;
    private LocalDateTime paidAt;
    @ElementCollection(targetClass = String.class)
    private Collection<String> attachedFile;

    @Column(
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT FALSE"
    )
    private Boolean isDeleted = false;
}
