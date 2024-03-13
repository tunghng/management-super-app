package com.im.form.model;

import com.im.form.model.enums.InputType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "input_text")
public class InputText {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private InputType type;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Text value;
}
