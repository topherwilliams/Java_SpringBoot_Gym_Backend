package com.example.assessment.Workout;

import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.MemberRepository;
import com.example.assessment.TestUtilityFunctions.TestUtilityFunctions;
import com.example.assessment.Workout.Entities.Workout;
import com.example.assessment.WorkoutExercise.DTOs.IncomingNewWorkoutExerciseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
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
    private WorkoutRepository workoutRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private TestUtilityFunctions testUtilityFunctions;

    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    @BeforeEach
    void resetRepositories() {
        testUtilityFunctions.clearAllRepositories();
    }

    //CREATE NEW WORKOUT USE CASE
    @Test
    void authenticatedMemberCreatesNewWorkoutForThemselves_expect_SuccessResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        mockMvc.perform(post("/workout/create/" + m.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member.member_id").value(m.getId()))
                .andExpect(jsonPath("$.exercises").isEmpty());
    }

    @Test
    void authenticatedMemberCreatesNewWorkoutForSomeoneElse_expect_AuthorisationResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        Member m2 = new Member(12, "testCreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        memberRepository.save(m2);
        mockMvc.perform(post("/workout/create/" + m2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthenticatedMemberCreatesNewWorkoutForSomeoneElse_expect_AuthorisationResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        memberRepository.save(m);
        mockMvc.perform(post("/workout/create/" + m.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // ADD EXERCISE TO WORKOUT USE CASE
    @Test
    void authenticatedMemberAddsExerciseToTheirWorkout_expect_SuccessResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        Workout w = new Workout(10, UUID.randomUUID().toString(), m, new ArrayList<>());
        workoutRepository.save(w);
        IncomingNewWorkoutExerciseDTO iDTO = new IncomingNewWorkoutExerciseDTO("Bicep Curl", 50, 10, 3);
        String expectedJson = ow.writeValueAsString(iDTO);
        mockMvc.perform(post("/workout/addexercise/" + w.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workout_id").value(w.getId()))
                .andExpect(jsonPath("$.member.member_id").value(m.getId()))
                .andExpect(jsonPath("$.exercises[0].exercise_name").value(iDTO.getExercise_name()));
    }

    @Test
    void authenticatedMemberAddsExerciseToSomeoneElsesWorkout_expect_AuthorisationResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        Member m2 = new Member(12, "test_emawner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m2);
        memberRepository.save(m);
        Workout w = new Workout(10, UUID.randomUUID().toString(), m, new ArrayList<>());
        workoutRepository.save(w);
        IncomingNewWorkoutExerciseDTO iDTO = new IncomingNewWorkoutExerciseDTO("Bicep Curl", 50, 10, 3);
        String expectedJson = ow.writeValueAsString(iDTO);
        mockMvc.perform(post("/workout/addexercise/" + w.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isUnauthorized());
    }
}