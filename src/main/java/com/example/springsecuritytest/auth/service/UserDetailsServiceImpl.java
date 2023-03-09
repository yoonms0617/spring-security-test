package com.example.springsecuritytest.auth.service;

import com.example.springsecuritytest.auth.domain.AuthMember;
import com.example.springsecuritytest.member.domain.Member;
import com.example.springsecuritytest.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("ERR005"));
        return new AuthMember(member.getId(), member.getEmail(), member.getPassword(), member.getRole().getKey());
    }

}
