package com.im.form.model;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "additions", callSuper = false)
@Table(name = "form_template")
public class FormTemplate extends BaseEntity implements Serializable {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "code")
    private FormTemplateCode code;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 10485760)
    private String logo;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "form_templates_additions",
            joinColumns = @JoinColumn(name = "form_template_id"),
            inverseJoinColumns = @JoinColumn(name = "addition_id")
    )
    private Collection<Addition> additions;

    private Boolean isPublic = true;

    @Column(
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT FALSE"
    )
    private Boolean isDeleted = false;

    @UpdateTimestamp
    private Date updatedAt;

    private UUID updateBy;

    private UUID tenantId;

    private UUID contactId;

    @Override
    public String toString() {
        return "FormTemplate{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", logo='" + logo + '\'' +
                ", isPublic=" + isPublic +
                ", isDeleted=" + isDeleted +
                ", updatedAt=" + updatedAt +
                ", updateBy=" + updateBy +
                ", tenantId=" + tenantId +
                ", contactId=" + contactId +
                '}';
    }
}
