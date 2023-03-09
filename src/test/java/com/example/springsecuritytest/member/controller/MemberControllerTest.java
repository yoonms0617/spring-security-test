package com.example.springsecuritytest.member.controller;

import com.example.springsecuritytest.auth.domain.AuthMember;
import com.example.springsecuritytest.auth.dto.LoginRequest;
import com.example.springsecuritytest.auth.support.JwtProvider;
import com.example.springsecuritytest.member.dto.MemberSignupRequest;
import com.example.springsecuritytest.member.service.MemberService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.willDoNothing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = MemberController.class,
        includeFilters = @ComponentScan.Filter(EnableWebSecurity.class)
)
@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    private static final String MEMBER_SIGNUP_REQUEST = "/api/member/signup";

    private static final String MEMBER_LOGIN_REQUEST = "/api/member/login";

    private static final String MEMBER_PROFILE_REQUEST = "/api/member/profile";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 요청을 보낸다 - 성공 200 Ok")
    void member_signup_valid_value_request() throws Exception {
        willDoNothing().given(memberService).signup(any());

        mockMvc.perform(post(MEMBER_SIGNUP_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockValidMemberSignupRequest()))
                )
                .andExpect(status().isOk())
                .andDo(print());

        then(memberService).should(times(1)).signup(any());
    }

    @Test
    @DisplayName("회원가입 요청을 보낸다. - 유효하지 않는 입력 값 400 Bad Request")
    void member_signup_invalid_value_request() throws Exception {
        willDoNothing().given(memberService).signup(any());

        mockMvc.perform(post(MEMBER_SIGNUP_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockInvalidMemberSignupRequest()))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

        then(memberService).should(times(0)).signup(any());
    }

    @Test
    @DisplayName("회원가입 요청을 보낸다. - 유효하지 않는 요청 메소드 405 Method Not Allowed")
    void member_signup_invalid_http_request_method_test() throws Exception {
        willDoNothing().given(memberService).signup(any());

        mockMvc.perform(get(MEMBER_SIGNUP_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockValidMemberSignupRequest()))
                )
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());

        then(memberService).should(times(0)).signup(any());
    }

    @Test
    @DisplayName("로그인 요청을 보낸다. - 성공 200 Ok")
    void member_login_valid_value_request_test() throws Exception {
        given(userDetailsService.loadUserByUsername(any())).willReturn(createMockUserDetails());
        given(jwtProvider.createAccessToken(any(), any(), any())).willReturn(createMockAccessToken());
        given(jwtProvider.createRefreshToken(any(), any(), any())).willReturn(createMockRefreshToken());

        mockMvc.perform(post(MEMBER_LOGIN_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockValidLoginRequest()))
                )
                .andExpect(status().isOk())
                .andDo(print());

        then(userDetailsService).should(times(1)).loadUserByUsername(any());
        then(jwtProvider).should(times(1)).createRefreshToken(any(), any(), any());
        then(jwtProvider).should(times(1)).createRefreshToken(any(), any(), any());
    }

    @Test
    @DisplayName("로그인 요청을 보낸다. - 잘못된 이메일 또는 비밀번호 400 Bad Request")
    void member_login_invalid_email_password_request_test() throws Exception {
        given(userDetailsService.loadUserByUsername(any())).willThrow(UsernameNotFoundException.class);

        mockMvc.perform(post(MEMBER_LOGIN_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockInValidLoginRequest()))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

        then(userDetailsService).should(times(1)).loadUserByUsername(any());
        then(jwtProvider).should(times(0)).createRefreshToken(any(), any(), any());
        then(jwtProvider).should(times(0)).createRefreshToken(any(), any(), any());
    }

    @Test
    @DisplayName("로그인 요청을 보낸다. - 유효하지 않는 요청 메소드 405 Method Not Allowed")
    void member_login_invalid_http_request_method_test() throws Exception {
        mockMvc.perform(get(MEMBER_LOGIN_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockValidLoginRequest()))
                )
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());

        then(userDetailsService).should(times(0)).loadUserByUsername(any());
        then(jwtProvider).should(times(0)).createRefreshToken(any(), any(), any());
        then(jwtProvider).should(times(0)).createRefreshToken(any(), any(), any());
    }

    private MemberSignupRequest createMockValidMemberSignupRequest() {
        return new MemberSignupRequest("윤민수", "yoon@test.com", "123456789");
    }

    private MemberSignupRequest createMockInvalidMemberSignupRequest() {
        return new MemberSignupRequest("", "yoon@test.com", "");
    }

    private LoginRequest createMockValidLoginRequest() {
        return new LoginRequest("yoon@test.com", "123456789");
    }

    private LoginRequest createMockInValidLoginRequest() {
        return new LoginRequest("yoon@test.com", "1234");
    }

    private AuthMember createMockUserDetails() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encoded = encoder.encode("123456789");
        return new AuthMember(1L, "yoon@test.com", encoded, "ROLE_MEMBER");
    }

    private String createMockAccessToken() {
        JwtProvider mockJwtProvider = new JwtProvider("hello-jwt-spring-security-test-hello-jwt-spring-security-test", 600000, 3600000);
        return mockJwtProvider.createAccessToken(1L, "yoon@test.com", "ROLE_MEMBER");
    }

    private String createMockRefreshToken() {
        JwtProvider mockJwtProvider = new JwtProvider("hello-jwt-spring-security-test-hello-jwt-spring-security-test", 600000, 3600000);
        return mockJwtProvider.createRefreshToken(1L, "yoon@test.com", "ROLE_MEMBER");
    }

}