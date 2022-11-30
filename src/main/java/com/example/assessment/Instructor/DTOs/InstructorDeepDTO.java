package com.example.assessment.Instructor.DTOs;

import com.example.assessment.FitnessClass.DTOs.FitnessClass_NoInstructor_DTO;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InstructorDeepDTO {
    private int instructor_id;
    private String instructor_name;
    private List<FitnessClass_NoInstructor_DTO> instructor_classes;
}
