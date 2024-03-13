package com.im.announcement.model;

import com.im.announcement.model.enums.PriorityType;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcement extends BaseEntity {

    @OneToMany(mappedBy = "announcement")
    Collection<AnnouncementContact> contacts;

    private String headline;

    @Column(length = 10485760)
    private String body;

    @Column(name = "announcement_code")
    private String code;

    @Column(
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT FALSE"
    )
    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private PriorityType priority;

    private UUID tenantId;

    @ElementCollection(targetClass = String.class)
    private Collection<String> attachedFile;
}
