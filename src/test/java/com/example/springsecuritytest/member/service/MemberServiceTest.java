package com.example.springsecuritytest.member.service;

import com.example.springsecuritytest.member.domain.Member;
import com.example.springsecuritytest.member.dto.MemberSignupRequest;
import com.example.springsecuritytest.member.exception.DuplicatEmailException;
import com.example.springsecuritytest.member.repository.MemberRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입을 한다.")
    void member_signup_test() {
        Member mockMember = createMockMember();
        MemberSignupRequest mockMemberSignupRequest = createMockMemberSignupRequest();
        String encoded = encryptionPassword();

        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(passwordEncoder.encode(any())).willReturn(encoded);
        given(memberRepository.save(any())).willReturn(mockMember);

        memberService.signup(mockMemberSignupRequest);

        then(memberRepository).should(times(1)).existsByEmail(any());
        then(passwordEncoder).should(times(1)).encode(any());
        then(memberRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("회원가입시 이메일이 중복되면 예외가 발생한다.")
    void member_signup_duplicate_email_test() {
        MemberSignupRequest mockMemberSignupRequest = createMockMemberSignupRequest();
        given(memberRepository.existsByEmail(any())).willReturn(true);

        assertThatThrownBy(() -> memberService.signup(mockMemberSignupRequest))
                .isInstanceOf(DuplicatEmailException.class);
        then(memberRepository).should(times(1)).existsByEmail(any());
        then(passwordEncoder).should(times(0)).encode(any());
        then(memberRepository).should(times(0)).save(any());
    }

    private MemberSignupRequest createMockMemberSignupRequest() {
        return new MemberSignupRequest("윤민수", "yoon@tet.com", "123456789");
    }

    private String encryptionPassword() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder.encode("123456789");
    }

    private Member createMockMember() {
        String encoded = encryptionPassword();
        Member member = Member.builder()
                .name("윤민수")
                .email("yoon@test.com")
                .password(encoded)
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);
        return member;
    }


}