package com.im.document.model;

import com.im.document.model.generator.CodeGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document_code")
public class DocumentCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "code_seq")
    @GenericGenerator(
            name = "code_seq",
            strategy = "com.im.document.model.generator.CodeGenerator",
            parameters = {
                    @Parameter(name = CodeGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = CodeGenerator.NUMBER_FORMAT_PARAMETER, value = "%07d")
            })
    private String id;
}
