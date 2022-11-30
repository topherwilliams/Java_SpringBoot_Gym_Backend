package com.example.assessment.WorkoutExercise.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkoutExerciseDTO {

    private String exercise_name;
    private int weight_kg;
    private int reps;
    private int sets;

}
