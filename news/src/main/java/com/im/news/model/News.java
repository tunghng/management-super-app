package com.im.news.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class News extends BaseEntity implements HasTenantId {
    @Column(name = "news_code")
    private String code;

    @Column(length = 10485760)
    private String cover;

    private String headline;

    @Column(length = 10485760)
    private String body;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "news_category_id")
    private NewsCategory category;

    @Column(
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT FALSE"
    )
    private Boolean isDeleted = false;

    private UUID tenantId;

    @ElementCollection(targetClass = String.class)
    private Collection<String> attachedFile;
}
