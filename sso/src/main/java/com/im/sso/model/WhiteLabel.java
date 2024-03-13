package com.im.sso.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WhiteLabel extends BaseEntity {

    @Column(length = 10485760)
    private String logoImage;

    @Column(length = 255)
    private String appTitle;

    private UUID tenantId;

}
