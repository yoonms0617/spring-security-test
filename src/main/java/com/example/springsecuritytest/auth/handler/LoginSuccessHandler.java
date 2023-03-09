package com.example.springsecuritytest.auth.handler;

import com.example.springsecuritytest.auth.domain.AuthMember;
import com.example.springsecuritytest.auth.dto.LoginResponse;
import com.example.springsecuritytest.auth.support.JwtProvider;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("LoginSuccessHandler");
        AuthMember principal = (AuthMember) authentication.getPrincipal();
        String accessToken = jwtProvider.createAccessToken(principal.getId(), principal.getEmail(), principal.getRole());
        String refreshToken = jwtProvider.createRefreshToken(principal.getId(), principal.getEmail(), principal.getRole());
        objectMapper.writeValue(response.getWriter(), new LoginResponse(accessToken, refreshToken));
    }

}
