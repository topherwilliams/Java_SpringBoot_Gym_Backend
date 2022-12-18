package com.example.assessment.Instructor;

import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.User.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {

    Instructor findByEmail(String email);

    Instructor findByToken(String token);
}
