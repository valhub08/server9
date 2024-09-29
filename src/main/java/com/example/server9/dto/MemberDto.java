package com.example.server9.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
public class MemberDto {

    @NotNull
    @Size(min = 3, max = 100)
    private String email;

    @NotNull
    @Size(min = 8, max = 300)
    private String password;

}