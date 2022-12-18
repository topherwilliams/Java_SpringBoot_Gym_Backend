package com.example.assessment.User.DTOs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class UserCredentialsDTO {

//    TODO: Have I updated all DTOs with validation messages - this might be why other validation not working - check

    @Email(message="Email is not in the correct format")
    private final String email;

    @NotBlank(message = "Password cannot be blank")
    private final String password;
}
