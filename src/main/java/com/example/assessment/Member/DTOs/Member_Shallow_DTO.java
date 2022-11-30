package com.example.assessment.Member.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Member_Shallow_DTO {

    private int member_id;
    private String member_email_address;
    private String member_username;
    private String member_name;

}
