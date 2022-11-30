package com.example.assessment.Workout;

import com.example.assessment.ClassBooking.ClassBookingRepository;
import com.example.assessment.FitnessClass.FitnessClassRepository;
import com.example.assessment.FitnessClass.FitnessClassService;
import com.example.assessment.Instructor.InstructorRepository;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.MemberRepository;
import com.example.assessment.Workout.DTOs.NewWorkoutDTO;
import com.example.assessment.Workout.Entities.Workout;
import com.example.assessment.WorkoutExercise.DTOs.NewWorkoutExerciseDTO;
import com.example.assessment.WorkoutExercise.WorkoutExerciseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WorkoutRestControllerIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;
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
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();


    //CREATE NEW WORKOUT USE CASE
    @Test
    void test_createNewWorkoutFromValidMemberId_expect_Response() throws Exception {
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        memberRepository.save(m);
        NewWorkoutDTO newWorkoutDTO = new NewWorkoutDTO(m.getId());
        String expectedJson = ow.writeValueAsString(newWorkoutDTO);

        mockMvc.perform(post("/workout/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workout_id").value(4))
                .andExpect(jsonPath("$.member.member_id").value(10))
                .andExpect(jsonPath("$.exercises").isEmpty())
        ;
    }

    @Test
    void test_createNewWorkoutFromInvalidMemberId_expect_EmptyResponse() throws Exception {
        clearAllRepositories();
        NewWorkoutDTO newWorkoutDTO = new NewWorkoutDTO(15);
        String expectedJson = ow.writeValueAsString(newWorkoutDTO);

        mockMvc.perform(post("/workout/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
        ;
    }

    // ADD EXERCISE TO WORKOUT USE CASE
    @Test
    void test_addNewExerciseToWorkoutWithValidWorkoutID_expect_WorkoutResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        memberRepository.save(m);
        Workout w = new Workout(10, UUID.randomUUID().toString(), m, new ArrayList<>());
        workoutRepository.save(w);
        NewWorkoutExerciseDTO newWorkoutExerciseDTO = new NewWorkoutExerciseDTO(w.getId(), "Bicep Curl", 50, 10, 3);
        String expectedJson = ow.writeValueAsString(newWorkoutExerciseDTO);

        mockMvc.perform(post("/workout/addexercise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workout_id").value(w.getId()))
                .andExpect(jsonPath("$.member.member_id").value(m.getId()))
                .andExpect(jsonPath("$.exercises[0].exercise_name").value(newWorkoutExerciseDTO.getExercise_name()))
        ;
    }

    @Test
    void test_addNewExerciseToWorkoutWithInvalidWorkoutID_expect_WorkoutResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        memberRepository.save(m);
        Workout w = new Workout(10, UUID.randomUUID().toString(), m, new ArrayList<>());
        workoutRepository.save(w);
        NewWorkoutExerciseDTO newWorkoutExerciseDTO = new NewWorkoutExerciseDTO(20, "Bicep Curl", 50, 10, 3);
        String expectedJson = ow.writeValueAsString(newWorkoutExerciseDTO);

        mockMvc.perform(post("/workout/addexercise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
        ;
    }


    void clearAllRepositories() {
        classBookingRepository.deleteAll();
        fitnessClassRepository.deleteAll();
        instructorRepository.deleteAll();
        workoutExerciseRepository.deleteAll();
        workoutRepository.deleteAll();
        memberRepository.deleteAll();
    }
}