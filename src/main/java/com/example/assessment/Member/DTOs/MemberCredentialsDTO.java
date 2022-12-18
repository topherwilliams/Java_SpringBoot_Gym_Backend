package com.example.assessment.Member.DTOs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class MemberCredentialsDTO {

    @Email(message="Email is not in the correct format")
    private final String email_address;

    @NotBlank(message = "Password cannot be blank")
    private final String password;
}
