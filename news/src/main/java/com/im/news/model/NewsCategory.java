package com.im.news.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCategory extends BaseEntity implements HasTenantId {
    private String name;
    private UUID tenantId;
}
