package com.example.assessment.Workout;


import com.example.assessment.Workout.DTOs.NewWorkoutDTO;
import com.example.assessment.Workout.DTOs.WorkoutDTO;
import com.example.assessment.WorkoutExercise.DTOs.IncomingNewWorkoutExerciseDTO;
import com.example.assessment.WorkoutExercise.DTOs.NewWorkoutExerciseDTO;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/workout")
@AllArgsConstructor
@Validated
public class WorkoutRestController {
    private final WorkoutService workoutService;

    @PostMapping(path="/create/{userID}")
    WorkoutDTO createNewWorkout(@PathVariable(name = "userID") int id) {
        NewWorkoutDTO newWorkoutDTO = new NewWorkoutDTO(id);
        return workoutService.createNewWorkout(newWorkoutDTO);
    };

    @PostMapping(path="/addexercise/{workoutID}")
    WorkoutDTO addExerciseToWorkout(@PathVariable(name = "workoutID") int id, @RequestBody @Valid IncomingNewWorkoutExerciseDTO inDTO) {
        NewWorkoutExerciseDTO n = new NewWorkoutExerciseDTO(id, inDTO.getExercise_name(), inDTO.getWeight_kg(), inDTO.getReps(), inDTO.getSets());
        return workoutService.addExerciseToWorkout(n);
    }

}
