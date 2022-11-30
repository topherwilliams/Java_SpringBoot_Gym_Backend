package com.example.assessment.FitnessClass.DTOs;

import com.example.assessment.ClassBooking.DTOs.ClassBooking_Member_DTO;
import com.example.assessment.Instructor.DTOs.InstructorShallowDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FitnessClass_All_DTO {
    private int class_id;
    private String class_name;
    private int class_duration;
    private int class_capacity;
    private int class_booked_spaces;
    private LocalDate class_date;
    private InstructorShallowDTO class_instructor;
    private List<ClassBooking_Member_DTO> class_attendees;
}
