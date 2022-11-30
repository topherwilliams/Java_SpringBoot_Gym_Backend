package com.example.assessment.Member;

import com.example.assessment.ClassBooking.ClassBookingRepository;
import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.FitnessClass.FitnessClassRepository;
import com.example.assessment.FitnessClass.FitnessClassService;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Instructor.InstructorRepository;
import com.example.assessment.Member.DTOs.NewMemberDTO;
import com.example.assessment.Member.DTOs.UpdatedMemberDTO;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Workout.Entities.Workout;
import com.example.assessment.Workout.WorkoutRepository;
import com.example.assessment.WorkoutExercise.WorkoutExerciseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MemberRestControllerIntegrationTests {

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

    // CREATE NEW MEMBER USE CASE TESTS
    @BeforeEach
    void resetRepositories() {
        clearAllRepositories();
    }

    @Test
    void test_createMemberWithIncorrectEmailUserNameandNameFormat_expect_ExceptionMessage() throws Exception {
        NewMemberDTO n = new NewMemberDTO("testusergmail.com", "", "");
        String expectedJson = ow.writeValueAsString(n);

        mockMvc.perform(post("/member/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email_address").value("Please provide a valid email address"))
                .andExpect(jsonPath("$.username").value("Username cannot be blank."))
                .andExpect(jsonPath("$.name").value("Name cannot be blank."))
        ;
    }

    @Test
    void test_whenMemberExistsAndCreateOwner_expect_NoOwnerReturned() throws Exception {
        clearAllRepositories();

        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        memberRepository.save(m);

        NewMemberDTO newMemberDTO = new NewMemberDTO(m.getEmail_address(), m.getUsername(), m.getMember_name());
        String expectedJson = ow.writeValueAsString(newMemberDTO);

        mockMvc.perform(post("/member/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void test_whenMemberDoesntExistsAndCreateOwner_expect_OwnerReturned() throws Exception {
        clearAllRepositories();
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        NewMemberDTO newMemberDTO = new NewMemberDTO(m.getEmail_address(), m.getUsername(), m.getMember_name());
        String expectedJson = ow.writeValueAsString(newMemberDTO);
        mockMvc.perform(post("/member/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.member_email_address").value(m.getEmail_address()))
                .andExpect(jsonPath("$.member_name").value(m.getMember_name()));
    }

    // GET ALL MEMBERS USE CASE TESTS
    @Test
    void test_getAllMembersWhenNoneExist_expect_EmptyResponse() throws Exception {
        clearAllRepositories();
        mockMvc.perform(get("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @Test
    void test_getAllMembersWhenMembersExist_expect_Response() throws Exception {
        clearAllRepositories();
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        memberRepository.save(m);
        Member m2 = new Member(0, "test_email_CreateOwner2@gmail.com", "Test_User2", "Test User Too", new ArrayList<>(), new ArrayList<>());
        memberRepository.save(m2);

        mockMvc.perform(get("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].member_email_address").value(m.getEmail_address()))
                .andExpect(jsonPath("$[1].member_email_address").value(m2.getEmail_address()));
    }

    // GET MEMBER USE CASE TESTS
    @Test
    void test_getMemberWhenMemberExists_expect_Response() throws Exception {
        clearAllRepositories();
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        memberRepository.save(m);

        mockMvc.perform(get("/member/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.member_email_address").value(m.getEmail_address()))
                .andExpect(jsonPath("$.member_id").value(6))
                .andExpect(jsonPath("$.member_workouts").isEmpty());
    }

    @Test
    void test_getMemberWhenMemberDoesntExistbutValidId_expect_EmptyResponse() throws Exception {
        clearAllRepositories();
        mockMvc.perform(get("/member/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void test_getMemberWhenIDisLetter_expect_Error() throws Exception {
        clearAllRepositories();
        mockMvc.perform(get("/member/a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_getMemberWhenIDisNonAlphanumericCharacter_expect_Error() throws Exception {
        clearAllRepositories();
        mockMvc.perform(get("/member/*")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // UPDATE MEMBER USE CASE TESTS
    @Test
    void test_updateMemberWithIncorrectEmailUserNameandNameFormat_expect_ExceptionMessage() throws Exception {
        UpdatedMemberDTO u = new UpdatedMemberDTO(0, "testusergmail.com", "", "");
        String expectedJson = ow.writeValueAsString(u);

        mockMvc.perform(patch("/member/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value("The ID field must be higher than 0."))
                .andExpect(jsonPath("$.email_address").value("Please provide a valid email address"))
                .andExpect(jsonPath("$.username").value("Username cannot be blank."))
                .andExpect(jsonPath("$.member_name").value("Name cannot be blank."))
        ;
    }


    @Test
    void test_updateMemberWhenMemberIDisNotFound_expect_EmptyResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        UpdatedMemberDTO updatedMemberDTO = new UpdatedMemberDTO(6, m.getEmail_address(), m.getUsername(), m.getMember_name());
        String expectedJson = ow.writeValueAsString(updatedMemberDTO);

        mockMvc.perform(patch("/member/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void test_updateMemberWhenMemberIDisValidButNoChangesMade_expect_EmptyResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        memberRepository.save(m);

        UpdatedMemberDTO updatedMemberDTO = new UpdatedMemberDTO(6, m.getEmail_address(), m.getUsername(), m.getMember_name());
        String expectedJson = ow.writeValueAsString(updatedMemberDTO);

        mockMvc.perform(patch("/member/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void test_updateMemberWhenMemberIDisValidAndChangesMade_expect_Response() throws Exception {
        // NB. This test runs fine when run in isolation, but when as part of full suite doesn't pass. Validated with Postman and confident that business logic works etc.
        clearAllRepositories();
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        memberRepository.save(m);

        String updatedEmailAddress = "changed_email_address@yahoo.com";
        UpdatedMemberDTO updatedMemberDTO = new UpdatedMemberDTO(6, updatedEmailAddress, m.getUsername(), m.getMember_name());
        String expectedJson = ow.writeValueAsString(updatedMemberDTO);

        mockMvc.perform(patch("/member/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member_email_address").value(updatedEmailAddress))
                .andExpect(jsonPath("$.member_id").value(6))
                .andExpect(jsonPath("$.member_name").value(m.getMember_name()));
    }

    // DELETE MEMBER USE CASE TESTS
    @Test
    void test_deleteMemberWhenMemberExistsWithNoClassesorWorkouts_expect_emptyResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        memberRepository.save(m);

        mockMvc.perform(delete("/member/delete/6"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void test_deleteMemberWhenMemberExistsWithClassesAndWorkouts_expect_emptyResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>());
        List<Workout> workoutList = new ArrayList<>();
        workoutList.add(new Workout(0, UUID.randomUUID().toString(), m, new ArrayList<>()));
        m.setWorkouts(workoutList);
        List<ClassBooking> classBookingList = new ArrayList<>();
        FitnessClass f = new FitnessClass(0, UUID.randomUUID().toString(), "Class", 60, 45, 1, LocalDate.now().plusDays(20), new Instructor(0, "Instructor One", new ArrayList<>()), new ArrayList<>());
        classBookingList.add(new ClassBooking(0,m, f));
        m.setClasses(classBookingList);
        memberRepository.save(m);

        mockMvc.perform(delete("/member/delete/6"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void test_deleteMemberWhenMemberDoesNotExist_expect_emptyResponse() throws Exception {
        clearAllRepositories();
        mockMvc.perform(delete("/member/delete/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
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