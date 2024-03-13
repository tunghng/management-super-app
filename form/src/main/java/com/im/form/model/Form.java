package com.im.form.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "template", callSuper = false)
@Table(name = "form")
public class Form extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "code")
    private FormCode code;

    @NotNull(message = "Form template is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    private FormTemplate template;

    @Column(length = 10485760)
    private String data;

    private UUID contactId;

    private Boolean isApproved = false;

    private Date approvedAt;

    private UUID approvedBy;

    private Boolean isRead = false;

    @Override
    public String toString() {
        return "Form{" +
                "code=" + code +
                ", template=" + template +
                ", data='" + data + '\'' +
                ", contactId=" + contactId +
                ", isApproved=" + isApproved +
                ", approvedAt=" + approvedAt +
                ", approvedBy=" + approvedBy +
                ", isRead=" + isRead +
                '}';
    }
}
