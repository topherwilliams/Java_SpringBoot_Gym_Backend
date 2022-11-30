package com.example.assessment.Workout.DTOs;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewWorkoutDTO {
    @NotNull
    @Min(value=1, message="Member ID must be a valid integer, more than 0.")
    private int member_id;

}
