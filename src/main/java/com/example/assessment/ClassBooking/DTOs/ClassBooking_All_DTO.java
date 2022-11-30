package com.example.assessment.ClassBooking.DTOs;

import com.example.assessment.FitnessClass.DTOs.FitnessClass_All_DTO;
import com.example.assessment.Member.DTOs.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClassBooking_All_DTO {

    private int booking_id;
    private MemberDTO member;
    private FitnessClass_All_DTO class_information;

}
