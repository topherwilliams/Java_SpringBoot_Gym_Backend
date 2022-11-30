package com.example.assessment.Member.DTOs;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewMemberDTO {
    @Email(message="Please provide a valid email address")
    private String email_address;
    @NotBlank(message="Username cannot be blank.")
    private String username;
    @NotBlank(message="Name cannot be blank.")
    private String name;
}
