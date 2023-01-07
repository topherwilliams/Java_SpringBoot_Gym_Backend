package com.example.assessment.UtilityFunctions;

import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Workout.DTOs.WorkoutDTO;
import com.example.assessment.Workout.Entities.Workout;
import com.example.assessment.WorkoutExercise.DTOs.WorkoutExerciseDTO;
import com.example.assessment.WorkoutExercise.Entities.WorkoutExercise;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutUtilitiesTest {

    static HashMap<Integer, String> exerciseMap = new HashMap<>() {{
        put(1, "Benchpress");
        put(2, "Squat");
        put(3, "Shoulder Press");
        put(4, "Bicep Curl");
    }};

    @Test
    void test_extractWorkoutExercisesFromWorkout_whereNoExercisesExist() {
        Workout w = returnWorkout();
        List<WorkoutExerciseDTO> comparisonDTO = WorkoutUtilities.extractWorkoutExerciseListFromWorkout(w.getExercises());
        assertTrue(comparisonDTO instanceof List<WorkoutExerciseDTO>);
        assertEquals(0, comparisonDTO.size());
    }

    @Test
    void test_extractWorkoutExercisesFromWorkout_whereExercisesExist() {
        Workout w = returnWorkout();
        List<WorkoutExercise> workoutExerciseList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            workoutExerciseList.add(new WorkoutExercise(i, exerciseMap.get(i), 15, 10, i, w));
        }
        w.setExercises(workoutExerciseList);
        List<WorkoutExerciseDTO> comparisonDTO = WorkoutUtilities.extractWorkoutExerciseListFromWorkout(w.getExercises());
        assertTrue(comparisonDTO instanceof List<WorkoutExerciseDTO>);
        assertEquals(4, comparisonDTO.size());
        assertEquals(10, comparisonDTO.get(0).getReps());
        assertEquals(exerciseMap.get(1), comparisonDTO.get(0).getExercise_name());
        assertEquals(exerciseMap.get(2), comparisonDTO.get(1).getExercise_name());
        assertEquals(exerciseMap.get(3), comparisonDTO.get(2).getExercise_name());
        assertEquals(exerciseMap.get(4), comparisonDTO.get(3).getExercise_name());
    }

    @Test
    void test_convertValidWorkoutToDTO() {
        Workout w = returnWorkout();
        WorkoutDTO comparisonDTO = WorkoutUtilities.convertWorkoutToDTO(w);
        assertTrue(comparisonDTO instanceof WorkoutDTO);
        assertEquals(w.getId(), comparisonDTO.getWorkout_id());
        assertEquals(w.getMember().getMember_name(), comparisonDTO.getMember().getMember_name());
    }

    @Test
    void test_convertInvalidWorkoutToDTO() {
        Workout w = new Workout();
        assertThrows(NullPointerException.class, () -> {
            WorkoutDTO wDTO = WorkoutUtilities.convertWorkoutToDTO(w);
        });
    }

    Workout returnWorkout() {
        Random random = new Random();
        int n = random.nextInt(4- 1) + 1;
        Member m = new Member(n, "Test_Member_"+n+"@gmail.com", "Test_User" + n, FitnessClassUtilitiesTest.nameMap.get(n), new ArrayList<>(), new ArrayList<>(), null, null);
        return new Workout(n, UUID.randomUUID().toString(), m, new ArrayList<>());
    }

}