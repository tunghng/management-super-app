package com.im.sso.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact implements HasTenantId {
    @Id
    private UUID id;

    private String name;

    private String taxNumber;

    private String email;

    private String phone;

    private String field;

    @Column(length = 10485760)
    private String description;

    @Column(length = 10485760)
    private String avatar;

    private Boolean isDeleted;

    private UUID tenantId;

    @OneToMany(mappedBy = "contact", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Collection<AppUser> users;

    private Date createdAt;

    private Date updatedAt;
}
