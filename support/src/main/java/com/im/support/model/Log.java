package com.im.support.model;

import com.im.support.model.enums.ActionStatus;
import com.im.support.model.enums.ActionType;
import com.im.support.model.enums.EntityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    private UUID tenantId;
    private UUID createdBy;
    private UUID entityId;
    @Enumerated(EnumType.STRING)
    private EntityType entityType;
    @Column(length = 10485760)
    private String actionData;
    @Enumerated(EnumType.STRING)
    private ActionStatus actionStatus;
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    @Column(length = 10485760)
    private String actionFailureDetails;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
