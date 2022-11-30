package com.example.assessment.ClassBooking.DTOs;

import com.example.assessment.FitnessClass.DTOs.FitnessClass_Shallow_DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClassBooking_FitnessClass_DTO {
    private int booking_id;
    private FitnessClass_Shallow_DTO class_information;
}
