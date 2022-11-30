package com.example.assessment.Workout.DTOs;

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
public class Workout_Shallow_DTO {

    private int workout_id;
    private List<WorkoutExerciseDTO> exercises;

}
