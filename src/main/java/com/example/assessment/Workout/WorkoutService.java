package com.example.assessment.Workout;

import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.MemberRepository;
import com.example.assessment.UtilityFunctions.WorkoutUtilities;
import com.example.assessment.Workout.DTOs.NewWorkoutDTO;
import com.example.assessment.Workout.DTOs.WorkoutDTO;
import com.example.assessment.Workout.Entities.Workout;
import com.example.assessment.WorkoutExercise.DTOs.NewWorkoutExerciseDTO;
import com.example.assessment.WorkoutExercise.Entities.WorkoutExercise;
import com.example.assessment.WorkoutExercise.WorkoutExerciseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final MemberRepository memberRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    WorkoutDTO createNewWorkout(NewWorkoutDTO n) {
        if(memberRepository.existsById(n.getMember_id())) {
            Member m = memberRepository.findById(n.getMember_id()).orElse(null);
            Workout w = new Workout(0, UUID.randomUUID().toString(), m,new ArrayList<>());
            workoutRepository.save(w);
            return WorkoutUtilities.convertWorkoutToDTO(workoutRepository.findWorkoutByUuid(w.getUuid()));
        } else {
            // member not found
            return null;
        }
    }

    WorkoutDTO addExerciseToWorkout(NewWorkoutExerciseDTO n) {
        if(workoutRepository.existsById(n.getWorkout_id())) {
            WorkoutExercise w = new WorkoutExercise(0, n.getExercise_name(), n.getWeight_kg(),
            n.getReps(), n.getSets(), workoutRepository.findById(n.getWorkout_id()).orElse(null));
            workoutExerciseRepository.save(w);
            return WorkoutUtilities.convertWorkoutToDTO(workoutRepository.findById(n.getWorkout_id()).orElse(null));
        }
        return null;
    }


}
