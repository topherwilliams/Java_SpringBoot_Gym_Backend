package com.example.assessment.WorkoutExercise;

import com.example.assessment.WorkoutExercise.Entities.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Integer> {


}
