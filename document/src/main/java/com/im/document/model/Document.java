package com.im.document.model;

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
public class Document extends BaseEntity {
    @Column(name = "document_code")
    private String code;

    private String title;

    @Column(length = 10485760)
    private String description;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_type_id")
    private DocumentType type;

    @Column(
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT FALSE"
    )
    private Boolean isDeleted = false;

    private UUID tenantId;

    @ElementCollection(targetClass = String.class)
    private Collection<String> attachedFile;

}
