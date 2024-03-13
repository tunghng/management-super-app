package com.im.form.model;

import com.im.form.model.generator.CodeGenerator;
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
@Table(name = "form_template_code")
public class FormTemplateCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "form_template_code_seq")
    @GenericGenerator(name = "form_template_code_seq", strategy = "com.im.form.model.generator.CodeGenerator", parameters = {
            @Parameter(name = CodeGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = CodeGenerator.VALUE_PREFIX_PARAMETER, value = ""),
            @Parameter(name = CodeGenerator.NUMBER_FORMAT_PARAMETER, value = "%07d")
    })
    private String id;
}
