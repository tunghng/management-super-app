package com.im.billing.model;

import com.im.billing.model.generator.CodeGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bill_code")
public class BillCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "code_seq")
    @GenericGenerator(
            name = "code_seq",
            strategy = "com.im.billing.model.generator.CodeGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = CodeGenerator.INCREMENT_PARAM, value = "1"),
                    @org.hibernate.annotations.Parameter(name = CodeGenerator.NUMBER_FORMAT_PARAMETER, value = "%07d")
            })
    private String id;
}
