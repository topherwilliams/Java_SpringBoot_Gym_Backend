package com.example.assessment.ClassBooking.DTOs;

import com.example.assessment.Member.DTOs.Member_Shallow_DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClassBooking_Member_DTO {
    private int booking_id;
    private Member_Shallow_DTO member_information;
}
