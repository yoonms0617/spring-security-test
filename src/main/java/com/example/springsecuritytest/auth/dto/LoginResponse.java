package com.example.springsecuritytest.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final String accessToken;

    private final String refreshToken;

    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
