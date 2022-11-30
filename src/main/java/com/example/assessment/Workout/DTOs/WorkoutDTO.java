package com.example.assessment.Workout.DTOs;

import com.example.assessment.Member.DTOs.Member_Shallow_DTO;
import com.example.assessment.WorkoutExercise.DTOs.WorkoutExerciseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkoutDTO {

    private int workout_id;
    private Member_Shallow_DTO member;
    private List<WorkoutExerciseDTO> exercises;

}
