package com.example.assessment.Workout;

import com.example.assessment.Workout.Entities.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Integer> {

    @Query("SELECT w FROM Workout w WHERE w.uuid = :uuid")
    Workout findWorkoutByUuid(@Param("uuid") String uuid);
}
