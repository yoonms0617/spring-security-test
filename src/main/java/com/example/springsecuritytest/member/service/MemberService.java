package com.example.springsecuritytest.member.service;

import com.example.springsecuritytest.common.error.exception.ErrorType;
import com.example.springsecuritytest.member.domain.Member;
import com.example.springsecuritytest.member.dto.MemberSignupRequest;
import com.example.springsecuritytest.member.exception.DuplicatEmailException;
import com.example.springsecuritytest.member.repository.MemberRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(MemberSignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicatEmailException(ErrorType.DUPLICATE_EMAIL);
        }
        String encoded = passwordEncoder.encode(request.getPassword());
        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encoded)
                .build();
        memberRepository.save(member);
    }


}
