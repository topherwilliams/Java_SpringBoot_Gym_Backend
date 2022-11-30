package com.example.assessment.Member.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatedMemberDTO {
    @NotNull
    @Min(value=1, message="The ID field must be higher than 0.")
    private int id;
    @Email(message="Please provide a valid email address")
    private String email_address;
    @NotBlank(message="Username cannot be blank.")
    private String username;
    @NotBlank(message="Name cannot be blank.")
    private String member_name;

}
