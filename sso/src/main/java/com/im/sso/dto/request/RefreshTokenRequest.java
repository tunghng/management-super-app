package com.im.sso.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotEmpty(message = "refresh token may not be empty")
    private String refreshToken;
}
