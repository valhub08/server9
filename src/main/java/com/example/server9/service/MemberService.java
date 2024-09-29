package com.example.server9.service;

import com.example.server9.domain.Member;
import com.example.server9.domain.MemberRepository;
import com.example.server9.dto.MemberDto;
import com.example.server9.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public Long join(MemberDto memberDto){
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))  //비밀번호 인코딩
                .roles(Collections.singletonList("ROLE_USER"))         //roles는 최초 USER로 설정
                .build();

        return memberRepository.save(member).getId();
    }

    @Transactional
    public String login(MemberDto memberDto){
        Member member = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        // 로그인에 성공하면 email, roles 로 토큰 생성 후 반환
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }
}