package com.example.springsecuritytest.member.controller;

import com.example.springsecuritytest.member.dto.MemberSignupRequest;
import com.example.springsecuritytest.member.service.MemberService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = MemberController.class,
        includeFilters = @ComponentScan.Filter(EnableWebSecurity.class)
)
class MemberControllerTest {

    private static final String MEMBER_SIGNUP_REQUEST = "/api/member/signup";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    void member_signup_valid_value_request() throws Exception {
        mockMvc.perform(post(MEMBER_SIGNUP_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockValidMemberSignupRequest()))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void member_signup_invalid_value_request() throws Exception {
        mockMvc.perform(post(MEMBER_SIGNUP_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockInvalidMemberSignupRequest()))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void member_signup_invalie_http_request_method_test() throws Exception {
        mockMvc.perform(get(MEMBER_SIGNUP_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockValidMemberSignupRequest()))
                )
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    private MemberSignupRequest createMockValidMemberSignupRequest() {
        return new MemberSignupRequest("윤민수", "yoon@test.com", "123456789");
    }

    private MemberSignupRequest createMockInvalidMemberSignupRequest() {
        return new MemberSignupRequest("", "yoon@test.com", "");
    }

}