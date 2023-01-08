package com.example.assessment.TestUtilityFunctions;

import com.example.assessment.AuthTokenClass.AuthTokenClass;
import com.example.assessment.ClassBooking.ClassBookingRepository;
import com.example.assessment.FitnessClass.FitnessClassRepository;
import com.example.assessment.FitnessClass.FitnessClassService;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Instructor.InstructorRepository;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.MemberRepository;
import com.example.assessment.Workout.WorkoutRepository;
import com.example.assessment.WorkoutExercise.WorkoutExerciseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

@Component
public class TestUtilityFunctions {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ClassBookingRepository classBookingRepository;
    @Autowired
    private FitnessClassRepository fitnessClassRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;
    @Autowired
    private WorkoutRepository workoutRepository;
    @Autowired
    private FitnessClassService fitnessClassService;
    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    public String generateAuthorisationHeader(Member m) throws JsonProcessingException {
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        return mapper.writeValueAsString(authToken);
    }

    public String generateAuthorisationHeader(Instructor i) throws JsonProcessingException {
        AuthTokenClass authToken = new AuthTokenClass(i.getEmail(), i.getPassword());
        return mapper.writeValueAsString(authToken);
    }

    public void clearAllRepositories() {
        classBookingRepository.deleteAll();
        fitnessClassRepository.deleteAll();
        instructorRepository.deleteAll();
        workoutExerciseRepository.deleteAll();
        workoutRepository.deleteAll();
        memberRepository.deleteAll();
    }


}
