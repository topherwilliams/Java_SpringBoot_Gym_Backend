package com.example.assessment.Member.DTOs;

import com.example.assessment.ClassBooking.DTOs.ClassBooking_FitnessClass_DTO;
import com.example.assessment.Workout.DTOs.Workout_Shallow_DTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberDTO {

    private int member_id;
    private String member_email_address;
    private String member_username;
    private String member_name;
    private List<ClassBooking_FitnessClass_DTO> member_booked_classes;
    private List<Workout_Shallow_DTO> member_workouts;


}
