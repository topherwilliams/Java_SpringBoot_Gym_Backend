package com.example.assessment.Instructor;

import com.example.assessment.Instructor.DTOs.InstructorCredentialsDTO;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.UtilityFunctions.StringHasher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final StringHasher stringHasher;

    public void clearToken(int instructorID) {
        Instructor i = instructorRepository.findById(instructorID).orElse(null);
        if (i != null) {
            i.setToken(null);
            instructorRepository.save(i);
        }
    }

    public Instructor checkCredentials(InstructorCredentialsDTO credentials) {
        Instructor i = instructorRepository.findByEmail(credentials.getEmail());
        if (i != null && i.getPassword().equals(credentials.getPassword())) {
            String token = stringHasher.hashString(UUID.randomUUID().toString() + ":" + LocalDate.now().toString());
            i.setToken(token);
            instructorRepository.save(i);
            return i;
        }
        return null;
    }

    public Instructor checkCredentials(String token) {
        Instructor i = instructorRepository.findByToken(token);
        if (i != null && i.getToken() != null) {
            return i;
        }
        return null;
    }


}
