package com.example.assessment.ClassBooking;
import com.example.assessment.ClassBooking.DTOs.IncomingClassBookingDTO;
import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.FitnessClass.FitnessClassRepository;
import com.example.assessment.FitnessClass.FitnessClassService;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Instructor.InstructorRepository;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.MemberRepository;
import com.example.assessment.AuthTokenClass.AuthTokenClass;
import com.example.assessment.TestUtilityFunctions.TestUtilityFunctions;
import com.example.assessment.Workout.WorkoutRepository;
import com.example.assessment.WorkoutExercise.WorkoutExerciseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ClassBookingRestControllerIntegrationTests {

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

    @Autowired
    private TestUtilityFunctions testUtilityFunctions;

    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    // BOOK FITNESS CLASS USE CASES

    @BeforeEach
    private void clearDatabases() {
        testUtilityFunctions.clearAllRepositories();
    }

//    public String generateAuthorisationHeader(Member m) throws JsonProcessingException {
//        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
//        return mapper.writeValueAsString(authToken);
//    }
//
//    public String generateAuthorisationHeader(Instructor i) throws JsonProcessingException {
//        AuthTokenClass authToken = new AuthTokenClass(i.getEmail(), i.getPassword());
//        return mapper.writeValueAsString(authToken);
//    }

    @Test
    void authenticatedMemberBooksClassForThemselvesWithInvalidClassID_Expect_EmptyResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        Instructor i = new Instructor(15, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        IncomingClassBookingDTO iDTO = new IncomingClassBookingDTO(10);
        String expectedJson = ow.writeValueAsString(iDTO);
        mockMvc.perform(post("/classbooking/create/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                ;
    }

    @Test
    void authenticatedMemberBooksClassForSomeoneElseWithValidClassID_Expect_AuthorisationResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        Instructor i = new Instructor(15, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        IncomingClassBookingDTO iDTO = new IncomingClassBookingDTO(8);
        String expectedJson = ow.writeValueAsString(iDTO);
        mockMvc.perform(post("/classbooking/create/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedMemberBooksClassForSomeoneElseWithInValidClassID_Expect_EmptyResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        Instructor i = new Instructor(15, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        IncomingClassBookingDTO iDTO = new IncomingClassBookingDTO(22);
        String expectedJson = ow.writeValueAsString(iDTO);
        mockMvc.perform(post("/classbooking/create/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void authenticatedMemberBooksClassForThemselvesWithValidClassID_Expect_SuccessResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        Instructor i = new Instructor(15, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        IncomingClassBookingDTO iDTO = new IncomingClassBookingDTO(8);
        String expectedJson = ow.writeValueAsString(iDTO);
        mockMvc.perform(post("/classbooking/create/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.class_information.class_name").value(f.getClass_name()))
        ;
    }

    // CANCEL BOOKING USE CASES

    @Test
    void test_cancelClassBookingForMyselfWithValidBookingID_expect_SuccessResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "testPassword1", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), "null", null, null));
        Instructor i = instructorRepository.findById(4).orElse(null);
        if (i != null) {
            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            ClassBooking c = new ClassBooking(6, m, f);
            classBookingRepository.save(c);
            mockMvc.perform(delete("/classbooking/cancel/6")
                            .header("Authorization", authorisationTokenJSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));
        }
    }

    @Test
    void cancelClassBookingForMyselfWithInValidBookingID_expect_AuthorisationResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "testPassword1", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), "null", null, null));
        Instructor i = instructorRepository.findById(4).orElse(null);
        if (i != null) {
            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            ClassBooking c = new ClassBooking(6, m, f);
            classBookingRepository.save(c);
            mockMvc.perform(delete("/classbooking/cancel/22")
                            .header("Authorization", authorisationTokenJSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void test_cancelClassBookingForSomeoneElseWithValidBookingID_expect_UnauthorisedResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "testPassword1", null);
        Member m2 = new Member(11, "test_email@gmail.com", "Test_User2", "Test User2", new ArrayList<>(), new ArrayList<>(), null, null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        memberRepository.save(m2);
        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), "null", null, null));
        Instructor i = instructorRepository.findById(4).orElse(null);
        if (i != null) {
            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            ClassBooking c = new ClassBooking(10, m2, f);
            classBookingRepository.save(c);
            mockMvc.perform(delete("/classbooking/cancel/11")
                            .header("Authorization", authorisationTokenJSON))
                    .andExpect(status().isUnauthorized())
                    ;
        }
    }

    @Test
    void unauthenticatedUserCancelsClassBookingForSomeoneElseWithValidBookingID_expect_UnauthorisedResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "testPassword1", null);
        Member m2 = new Member(11, "test_email@gmail.com", "Test_User2", "Test User2", new ArrayList<>(), new ArrayList<>(), null, null);
        memberRepository.save(m);
        memberRepository.save(m2);
        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), "null", null, null));
        Instructor i = instructorRepository.findById(4).orElse(null);
        if (i != null) {
            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            ClassBooking c = new ClassBooking(10, m2, f);
            classBookingRepository.save(c);
            mockMvc.perform(delete("/classbooking/cancel/10"))
                    .andExpect(status().isUnauthorized())
            ;
        }
    }

    // VIEW UPCOMING CLASSES FOR MEMBER USE CASES
    @Test
    void test_viewUpcomingClassesForMyselfWithValidMemberId_expect_Response() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), "null@null.com", null, null));
        Instructor i = instructorRepository.findById(4).orElse(null);
        if (i != null) {
            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            ClassBooking c = new ClassBooking(10, m, f);
            classBookingRepository.save(c);
            mockMvc.perform(get("/classbooking/attendee/10/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", authorisationTokenJSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].booking_id").value(c.getId()))
                    .andExpect(jsonPath("$[0].class_information.class_id").value(f.getId()))
                    .andExpect(jsonPath("$[0].class_information.class_name").value(f.getClass_name()));
        }
    }

    @Test
    void test_viewUpcomingClassesForSomeoneElseWithValidMemberId_expect_UnauthorisedResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), "null@null.com", null, null));
        Instructor i = instructorRepository.findById(4).orElse(null);
        if (i != null) {
            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            ClassBooking c = new ClassBooking(10, m, f);
            classBookingRepository.save(c);
            mockMvc.perform(get("/classbooking/attendee/11")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", authorisationTokenJSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void viewUpcomingClassesWithNoAuthorisationHeader_expect_UnauthorisedResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        memberRepository.save(m);
        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), "null@null.com", null, null));
        Instructor i = instructorRepository.findById(4).orElse(null);
        if (i != null) {
            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            ClassBooking c = new ClassBooking(10, m, f);
            classBookingRepository.save(c);
            mockMvc.perform(get("/classbooking/attendee/11")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void authenticatedInstructorRequestsMemberClasses_expect_UnauthorisedResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        memberRepository.save(m);
        instructorRepository.save(new Instructor(88, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null));
        Instructor i = instructorRepository.findById(88).orElse(null);
        if (i != null) {
            String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            ClassBooking c = new ClassBooking(10, m, f);
            classBookingRepository.save(c);
            mockMvc.perform(get("/classbooking/attendee/10")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", authorisationTokenJSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].booking_id").value(c.getId()));
        }
    }

//    public void clearAllRepositories() {
//        classBookingRepository.deleteAll();
//        fitnessClassRepository.deleteAll();
//        instructorRepository.deleteAll();
//        workoutExerciseRepository.deleteAll();
//        workoutRepository.deleteAll();
//        memberRepository.deleteAll();
//    }

}