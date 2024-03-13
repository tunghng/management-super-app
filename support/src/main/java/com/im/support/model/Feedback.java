package com.im.support.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback extends BaseEntity {

    private String title;

    @Column(length = 10485760)
    private String body;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ElementCollection(targetClass = String.class)
    private Collection<String> attachedFile;
}
