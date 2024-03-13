package com.im.form.dto.model;

import com.im.form.model.enums.InputType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InputTextDto {
    private InputType type;
    private TextDto value;
}