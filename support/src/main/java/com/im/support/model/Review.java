package com.im.support.model;

import com.im.support.model.enums.ReviewTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ReviewTitle title;

    @Column(length = 10485760)
    private String body;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
