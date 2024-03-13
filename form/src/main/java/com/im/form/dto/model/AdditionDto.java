package com.im.form.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdditionDto {
    private InputTextDto inputText;
    private String title;
    @Column(length = 1000)
    private String description;
    private Boolean required;
}
