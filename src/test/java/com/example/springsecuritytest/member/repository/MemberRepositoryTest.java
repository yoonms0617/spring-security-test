package com.example.springsecuritytest.member.repository;

import com.example.springsecuritytest.common.config.JpaAuditConfig;
import com.example.springsecuritytest.member.domain.Member;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(JpaAuditConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void member_save_test() {
        Member mockMember = createMockMember();

        Member save = memberRepository.save(mockMember);

        checkMemberInfo(save, mockMember);
    }

    @Test
    void member_findById_test() {
        Member mockMember = createMockMember();
        memberRepository.save(mockMember);

        Member find = memberRepository.findById(mockMember.getId()).orElseThrow();

        checkMemberInfo(find, mockMember);
    }

    @Test
    void member_findByEmail_test() {
        Member mockMember = createMockMember();
        memberRepository.save(mockMember);

        Member find = memberRepository.findByEmail(mockMember.getEmail()).orElseThrow();

        checkMemberInfo(find, mockMember);
    }

    @Test
    void member_existsByEmail_test() {
        Member mockMember = createMockMember();
        memberRepository.save(mockMember);

        boolean existsTrue = memberRepository.existsByEmail(mockMember.getEmail());
        boolean existsFalse = memberRepository.existsByEmail("notexists@email.com");

        assertThat(existsTrue).isTrue();
        assertThat(existsFalse).isFalse();
    }

    private Member createMockMember() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encoded = encoder.encode("12345678");
        return Member.builder()
                .name("윤민수")
                .email("yoon@test.com")
                .password(encoded)
                .build();
    }

    private void checkMemberInfo(Member actual, Member expected) {
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getRole()).isEqualTo(expected.getRole());
    }

}