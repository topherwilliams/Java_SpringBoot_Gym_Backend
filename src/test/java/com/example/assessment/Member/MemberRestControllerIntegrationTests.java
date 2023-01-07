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
import com.example.assessment.UtilityFunctions.AuthTokenClass;
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

    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

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

        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
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
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
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
    void test_getAllMembersWhenMembersExist_expect_Response() throws Exception {
        clearAllRepositories();
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        Member m2 = new Member(0, "test_email_CreateOwner2@gmail.com", "Test_User2", "Test User Too", new ArrayList<>(), new ArrayList<>(), "null", null);
        memberRepository.save(m);
        memberRepository.save(m2);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
        mockMvc.perform(get("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].member_email_address").value(m.getEmail_address()))
                .andExpect(jsonPath("$[1].member_email_address").value(m2.getEmail_address()));
    }

    @Test
    void unauthenticatedUserRequestsMembers_expect_AuthorisationResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        Member m2 = new Member(0, "test_email_CreateOwner2@gmail.com", "Test_User2", "Test User Too", new ArrayList<>(), new ArrayList<>(), "null", null);
        memberRepository.save(m);
        memberRepository.save(m2);
        mockMvc.perform(get("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // GET MEMBER USE CASE TESTS
    @Test
    void authenticatedMemberRequestsTheirInfo_expect_Response() throws Exception {
        clearAllRepositories();
        Member m = new Member(6, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        memberRepository.save(m);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
        mockMvc.perform(get("/member/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.member_email_address").value(m.getEmail_address()))
                .andExpect(jsonPath("$.member_id").value(6))
                .andExpect(jsonPath("$.member_workouts").isEmpty());
    }

    @Test
    void authenticatedMemberRequestsAnotherMembersInfo_expect_Response() throws Exception {
        //TODO: In hindsight, might not be wise to allow members to see the other members? Future development.
        clearAllRepositories();
        Member m = new Member(6, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        Member m2 = new Member(8, "test_email_Crea@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        memberRepository.save(m);
        memberRepository.save(m2);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
        mockMvc.perform(get("/member/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.member_email_address").value(m2.getEmail_address()))
                .andExpect(jsonPath("$.member_id").value(8))
                .andExpect(jsonPath("$.member_workouts").isEmpty());
    }


    @Test
    void authenticatedInstructorRequestsAnotherMemberInfo_expect_Response() throws Exception {
        clearAllRepositories();
        Member m = new Member(6, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        memberRepository.save(m);
        Instructor i = new Instructor(0, "Instructor One", new ArrayList<>(), "nul@null.com", "null", null);
        instructorRepository.save(i);
        AuthTokenClass authToken = new AuthTokenClass(i.getEmail(), i.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
        mockMvc.perform(get("/member/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.member_email_address").value(m.getEmail_address()))
                .andExpect(jsonPath("$.member_id").value(6))
                .andExpect(jsonPath("$.member_workouts").isEmpty());
    }

    @Test
    void unauthenticatedUserRequestsAnotherMemberInfo_expect_Response() throws Exception {
        clearAllRepositories();
        Member m = new Member(6, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        memberRepository.save(m);
        mockMvc.perform(get("/member/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                ;
    }


    @Test
    void authorisedMemberRequestsInvalidCharacterMemberID_expect_Error() throws Exception {
        clearAllRepositories();
        Member m = new Member(6, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        memberRepository.save(m);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
        mockMvc.perform(get("/member/a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_getMemberWhenIDisNonAlphanumericCharacter_expect_Error() throws Exception {
        clearAllRepositories();
        Member m = new Member(6, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "null", null);
        memberRepository.save(m);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
        mockMvc.perform(get("/member/*")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isBadRequest());
    }


    //TODO: YOU ARE HERE
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
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
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
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
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
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
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
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
        memberRepository.save(m);

        mockMvc.perform(delete("/member/delete/6"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void test_deleteMemberWhenMemberExistsWithClassesAndWorkouts_expect_emptyResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
        List<Workout> workoutList = new ArrayList<>();
        workoutList.add(new Workout(0, UUID.randomUUID().toString(), m, new ArrayList<>()));
        m.setWorkouts(workoutList);
        List<ClassBooking> classBookingList = new ArrayList<>();
        FitnessClass f = new FitnessClass(0, UUID.randomUUID().toString(), "Class", 60, 45, 1, LocalDate.now().plusDays(20), new Instructor(0, "Instructor One", new ArrayList<>(), "nul@null.com", "null", null), new ArrayList<>());
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