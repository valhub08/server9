package com.example.server9.controller;

import com.example.server9.dto.MemberDto;
import com.example.server9.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginMemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public String login(@RequestBody MemberDto memberDto) {
        return memberService.login(memberDto);
    }
}
