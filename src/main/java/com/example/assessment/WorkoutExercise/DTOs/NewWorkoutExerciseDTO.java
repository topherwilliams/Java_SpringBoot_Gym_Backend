package com.example.assessment.WorkoutExercise.DTOs;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewWorkoutExerciseDTO {
    @NotNull
    @Min(1)
    private int workout_id;
    @NotBlank
    private String exercise_name;
    @NotNull
    @Min(1)
    private int weight_kg;
    @NotNull
    @Min(1)
    private int reps;
    @NotNull
    @Min(1)
    private int sets;
}
