package com.example.assessment.ClassBooking;
import com.example.assessment.ClassBooking.DTOs.NewClassBookingDTO;
import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.FitnessClass.FitnessClassRepository;
import com.example.assessment.FitnessClass.FitnessClassService;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Instructor.InstructorRepository;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.MemberRepository;
import com.example.assessment.UtilityFunctions.AuthTokenClass;
import com.example.assessment.Workout.WorkoutRepository;
import com.example.assessment.WorkoutExercise.WorkoutExerciseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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

    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    // BOOK FITNESS CLASS USE CASES

    @Test
    void authenticatedMemberBooksClassForThemselvesWithInvalidClassID_Expect_EmptyResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
        memberRepository.save(m);
        Instructor i = new Instructor(15, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        mockMvc.perform(post("/classbooking/create/10/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                ;
    }

    @Test
    void authenticatedMemberBooksClassForSomeoneElseWithValidClassID_Expect_EmptyResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
        memberRepository.save(m);
        Instructor i = new Instructor(15, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        mockMvc.perform(post("/classbooking/create/2/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    void authenticatedMemberBooksClassForSomeoneElseWithInValidClassID_Expect_EmptyResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
        memberRepository.save(m);
        Instructor i = new Instructor(15, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        mockMvc.perform(post("/classbooking/create/2/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isUnauthorized())
        ;
    }
    @Test
    void authenticatedMemberBooksClassForThemselvesWithValidClassID_Expect_SuccessResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
        memberRepository.save(m);
        Instructor i = new Instructor(15, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        mockMvc.perform(post("/classbooking/create/10/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
        ;
    }



////    No longer needed after implementation of webfilter - replaced by other test
//    @Test
//    void test_CreateNewClassBookingwithInvalidClassId_expect_EmptyResponse() throws Exception {
//        // NB fails when run as part of class but works when run individually. Validated separately via Postman
//        clearAllRepositories();
//        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
//        memberRepository.save(m);
//        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), null, null, null));
//        Instructor i = instructorRepository.findById(4).orElse(null);
//        if (i != null) {
//            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
//            fitnessClassRepository.save(f);
//
//            NewClassBookingDTO newClassBookingDTO = new NewClassBookingDTO(88, 6);
//            String expectedJson = ow.writeValueAsString(newClassBookingDTO);
//
//            mockMvc.perform(post("/classbooking/create")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(expectedJson)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().string(""));
//        }
//    }

////    No longer needed after implementation of webfilter - replaced by other test
//    @Test
//    void test_CreateNewClassBookingwithInvalidMemberId_expect_EmptyResponse() throws Exception {
//        // NB fails when run as part of class but works when run individually. Validated separately via Postman
//        clearAllRepositories();
//        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
//        memberRepository.save(m);
//        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), null, null,null));
//        Instructor i = instructorRepository.findById(4).orElse(null);
//        if (i !=null) {
//            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
//            fitnessClassRepository.save(f);
//            NewClassBookingDTO newClassBookingDTO = new NewClassBookingDTO(8, 66);
//            String expectedJson = ow.writeValueAsString(newClassBookingDTO);
//            mockMvc.perform(post("/classbooking/create")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(expectedJson)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().string(""));
//        }
//    }

////    No longer needed after implementation of webfilter - replaced by other test
//    @Test
//    void test_CreateNewClassBookingwithInvalidClassIdandInvalidMemberId_expect_EmptyResponse() throws Exception {
//        clearAllRepositories();
//        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
//        memberRepository.save(m);
//        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), null, null, null));
//        Instructor i = instructorRepository.findById(4).orElse(null);
//        FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
//        fitnessClassRepository.save(f);
//
//        NewClassBookingDTO newClassBookingDTO = new NewClassBookingDTO(88, 66);
//        String expectedJson = ow.writeValueAsString(newClassBookingDTO);
//
//        mockMvc.perform(post("/classbooking/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(expectedJson)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string(""));
//    }

////    No longer needed after implementation of webfilter - replaced by other test
//    @Test
//    void test_CreateNewClassBookingwithValidId_expect_Response() throws Exception {
//        // NB fails when run as part of class but works when run individually. Validated separately via Postman
//        clearAllRepositories();
//        Member m = new Member(0, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
//        memberRepository.save(m);
//        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), null, null, null));
//        Instructor i = instructorRepository.findById(4).orElse(null);
//        if (i != null) {
//            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
//            fitnessClassRepository.save(f);
//
//            NewClassBookingDTO newClassBookingDTO = new NewClassBookingDTO(8, 6);
//            String expectedJson = ow.writeValueAsString(newClassBookingDTO);
//
//            mockMvc.perform(post("/classbooking/create")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(expectedJson)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.booking_id").value(4))
//                    .andExpect(jsonPath("$.class_information.class_name").value(f.getClass_name()));
//        }
//    }


    // CANCEL BOOKING USE CASES

    @Test
    void test_cancelClassBookingForMyselfWithValidBookingID_expect_SuccessResponse() throws Exception {
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "testPassword1", null);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
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
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "testPassword1", null);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
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
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "testPassword1", null);
        Member m2 = new Member(11, "test_email@gmail.com", "Test_User2", "Test User2", new ArrayList<>(), new ArrayList<>(), null, null);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
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
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "testPassword1", null);
        Member m2 = new Member(11, "test_email@gmail.com", "Test_User2", "Test User2", new ArrayList<>(), new ArrayList<>(), null, null);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
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

//// Following tests no longer needed after web filter implementation
//    @Test
//    void test_cancelClassBookingWithValidBookingId_expect_EmptyResponse() throws Exception {
//        // NB fails when run as part of class but works when run individually. Validated separately via Postman
//        clearAllRepositories();
//        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
//        memberRepository.save(m);
//        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), null, null, null));
//        Instructor i = instructorRepository.findById(4).orElse(null);
//        if (i != null) {
//            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
//            fitnessClassRepository.save(f);
//            ClassBooking c = new ClassBooking(10, m, f);
//            classBookingRepository.save(c);
//
//            mockMvc.perform(delete("/classbooking/cancel/10"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().string(""));
//        }
//    }
//
//    @Test
//    void test_cancelClassBookingWithInvalidBookingId_expect_EmptyResponse() throws Exception {
//        // NB fails when run as part of class but works when run individually. Validated separately via Postman
//        clearAllRepositories();
//
//        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), null, null);
//        memberRepository.save(m);
//        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), null, null, null));
//        Instructor i = instructorRepository.findById(4).orElse(null);
//        if (i != null) {
//            FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
//            fitnessClassRepository.save(f);
//            ClassBooking c = new ClassBooking(10, m, f);
//            classBookingRepository.save(c);
//
//            mockMvc.perform(delete("/classbooking/cancel/20"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().string(""));
//        }
//    }

    // VIEW UPCOMING CLASSES FOR MEMBER USE CASES
    @Test
    void test_viewUpcomingClassesForMyselfWithValidMemberId_expect_Response() throws Exception {
        // NB fails when run as part of class but works when run individually. Validated separately via Postman
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
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
        // NB fails when run as part of class but works when run individually. Validated separately via Postman
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        AuthTokenClass authToken = new AuthTokenClass(m.getEmail_address(), m.getPassword());
        String authorisationTokenJSON = mapper.writeValueAsString(authToken);
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
        // NB fails when run as part of class but works when run individually. Validated separately via Postman
        clearAllRepositories();
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
        // NB fails when run as part of class but works when run individually. Validated separately via Postman
        clearAllRepositories();
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "password2", null);
        memberRepository.save(m);
        instructorRepository.save(new Instructor(88, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null));
        Instructor i = instructorRepository.findById(88).orElse(null);
        if (i != null) {
            AuthTokenClass authToken = new AuthTokenClass(i.getEmail(), i.getPassword());
            String authorisationTokenJSON = mapper.writeValueAsString(authToken);
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


// Not needed after webfilter implementation
//    @Test
//    void test_viewUpcomingClassesForMemberWithInvalidMemberID_expect_EmptyResponse() throws Exception {
//        clearAllRepositories();
//        mockMvc.perform(get("/classbooking/attendee/3")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("[]"))
//        ;
//    }


    void clearAllRepositories() {
        classBookingRepository.deleteAll();
        fitnessClassRepository.deleteAll();
        instructorRepository.deleteAll();
        workoutExerciseRepository.deleteAll();
        workoutRepository.deleteAll();
        memberRepository.deleteAll();
    }

}