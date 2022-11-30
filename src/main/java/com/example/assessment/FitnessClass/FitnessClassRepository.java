package com.example.assessment.FitnessClass;

import com.example.assessment.FitnessClass.Entities.FitnessClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FitnessClassRepository extends JpaRepository<FitnessClass, Integer> {

    @Query("SELECT f FROM FitnessClass f WHERE f.uuid = :uuid")
    FitnessClass findFitnessClassByUuidEquals(@Param(value="uuid") String uuid);

    @Query("SELECT f FROM FitnessClass f WHERE f.instructor.id = :instructor_id")
    List<FitnessClass> findFitnessClassesByInstructorID(@Param(value="instructor_id") int instructor_id);

}
