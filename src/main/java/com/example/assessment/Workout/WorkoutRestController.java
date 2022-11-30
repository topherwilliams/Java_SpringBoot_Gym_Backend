package com.example.assessment.Workout;


import com.example.assessment.Workout.DTOs.NewWorkoutDTO;
import com.example.assessment.Workout.DTOs.WorkoutDTO;
import com.example.assessment.WorkoutExercise.DTOs.NewWorkoutExerciseDTO;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/workout")
@AllArgsConstructor
@Validated
public class WorkoutRestController {
    private final WorkoutService workoutService;

    @PostMapping(path="/create")
    WorkoutDTO createNewWorkout(@RequestBody @Valid NewWorkoutDTO newWorkoutDTO) {
        return workoutService.createNewWorkout(newWorkoutDTO);
    };

    @PostMapping(path="/addexercise")
    WorkoutDTO addExerciseToWorkout(@RequestBody @Valid NewWorkoutExerciseDTO newWorkoutExerciseDTO) {
        return workoutService.addExerciseToWorkout(newWorkoutExerciseDTO);
    }

}
