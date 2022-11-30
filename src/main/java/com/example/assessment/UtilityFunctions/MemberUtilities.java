package com.example.assessment.UtilityFunctions;

import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.ClassBooking.DTOs.ClassBooking_FitnessClass_DTO;
import com.example.assessment.FitnessClass.DTOs.FitnessClass_Shallow_DTO;
import com.example.assessment.Instructor.DTOs.InstructorShallowDTO;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.DTOs.MemberDTO;
import com.example.assessment.Workout.DTOs.Workout_Shallow_DTO;
import com.example.assessment.Workout.Entities.Workout;
import com.example.assessment.Workout.DTOs.WorkoutDTO;

import java.util.ArrayList;
import java.util.List;

public class MemberUtilities {

    public static List<ClassBooking_FitnessClass_DTO> extractBookedFitnessClassDTOsFromMember(List<ClassBooking> classBookingList) {
        List<ClassBooking_FitnessClass_DTO> bookedClasses = new ArrayList<>();
        for (ClassBooking c : classBookingList) {
            FitnessClass_Shallow_DTO fitnessClassDTO = new FitnessClass_Shallow_DTO(c.getFitnessClass().getId(),
                    c.getFitnessClass().getClass_name(),
                    c.getFitnessClass().getDuration(),
                    c.getFitnessClass().getSpaces(),
                    c.getFitnessClass().getBooked_spaces(),
                    c.getFitnessClass().getClass_date(),
                    new InstructorShallowDTO(c.getFitnessClass().getInstructor().getId(), c.getFitnessClass().getInstructor().getInstructor_name())
            );
            bookedClasses.add(new ClassBooking_FitnessClass_DTO(c.getId(), fitnessClassDTO));
        }
        return bookedClasses;
    };

    public static List<Workout_Shallow_DTO> extractWorkoutsfromMember(List<Workout> workoutList) {
        List<Workout_Shallow_DTO> tempWorkoutList = new ArrayList<>();
        for(Workout w : workoutList) {
            tempWorkoutList.add(new Workout_Shallow_DTO(w.getId(), WorkoutUtilities.extractWorkoutExerciseListFromWorkout(w.getExercises())));
        };
        return tempWorkoutList;
    };


    public static MemberDTO convertMemberToMemberDTO(Member m) {
        // Create list of Fitness Class DTOs
        List<ClassBooking_FitnessClass_DTO> bookedClasses = MemberUtilities.extractBookedFitnessClassDTOsFromMember(m.getClasses());
        // Create list of Workout DTOs
        List<Workout_Shallow_DTO> workoutList = MemberUtilities.extractWorkoutsfromMember(m.getWorkouts());
        return new MemberDTO(m.getId(), m.getEmail_address(), m.getUsername(), m.getMember_name(), bookedClasses, workoutList);
    }



}
