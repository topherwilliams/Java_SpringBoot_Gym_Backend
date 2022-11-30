package com.example.assessment.UtilityFunctions;

import com.example.assessment.Member.DTOs.Member_Shallow_DTO;
import com.example.assessment.Workout.DTOs.WorkoutDTO;
import com.example.assessment.Workout.Entities.Workout;
import com.example.assessment.WorkoutExercise.DTOs.WorkoutExerciseDTO;
import com.example.assessment.WorkoutExercise.Entities.WorkoutExercise;

import java.util.ArrayList;
import java.util.List;

public class WorkoutUtilities {

    public static WorkoutDTO convertWorkoutToDTO(Workout w) {
        List<WorkoutExerciseDTO> exerciseDTOList = extractWorkoutExerciseListFromWorkout(w.getExercises());
        Member_Shallow_DTO member_shallow_dto = new Member_Shallow_DTO(w.getMember().getId(),
                w.getMember().getEmail_address(), w.getMember().getUsername(), w.getMember().getMember_name());
        return new WorkoutDTO(w.getId(), member_shallow_dto, exerciseDTOList);
    }

    public static List<WorkoutExerciseDTO> extractWorkoutExerciseListFromWorkout(List<WorkoutExercise> w) {
        List<WorkoutExerciseDTO> exerciseDTOList = new ArrayList<>();
        for (WorkoutExercise e : w) {
            WorkoutExerciseDTO tempWorkoutExerciseDTO = new WorkoutExerciseDTO(
                    e.getExercise_name(),
                    e.getWeight_kg(),
                    e.getReps(),
                    e.getExercise_sets()
            );
            exerciseDTOList.add(tempWorkoutExerciseDTO);
        }
        return exerciseDTOList;
    }
}
