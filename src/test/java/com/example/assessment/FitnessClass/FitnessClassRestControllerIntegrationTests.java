package com.example.assessment.FitnessClass;

import com.example.assessment.FitnessClass.DTOs.NewFitnessClassDTO;
import com.example.assessment.FitnessClass.DTOs.UpdatedFitnessClassDTO;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Instructor.InstructorRepository;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.MemberRepository;
import com.example.assessment.TestUtilityFunctions.TestUtilityFunctions;
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
class FitnessClassRestControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FitnessClassRepository fitnessClassRepository;

    @Autowired
    private InstructorRepository instructorRepository;


    @Autowired
    MockMvc mockMvc;

    @Autowired
    private TestUtilityFunctions testUtilityFunctions;

    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    @BeforeEach
    private void clearDatabases() {
        testUtilityFunctions.clearAllRepositories();
    }

    // CREATE NEW FITNESS CLASS USE CASES

    @Test
    void createNewFitnessClassFromValidInstructorForSameInstructor_expect_SuccessResponse() throws Exception {
        Instructor i = new Instructor(12, "Instructor Test", new ArrayList<>(), "null@null.com", "pass", null);
        instructorRepository.save(i);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        NewFitnessClassDTO n = new NewFitnessClassDTO(12, "Test New Fitness Class", 60, 20, LocalDate.now().plusDays(20));
        String expectedJson = ow.writeValueAsString(n);
        mockMvc.perform(post("/fitnessclass/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.class_name").value(n.getClass_name()));
    }

    @Test
    void createNewFitnessClassFromValidInstructorForSameInstructorWithoutAuthentication_expect_AuthorisationResponse() throws Exception {
        Instructor i = new Instructor(12, "Instructor Test", new ArrayList<>(), "null@null.com", "pass", null);
        instructorRepository.save(i);
        NewFitnessClassDTO n = new NewFitnessClassDTO(12, "Test New Fitness Class", 60, 20, LocalDate.now().plusDays(20));
        String expectedJson = ow.writeValueAsString(n);
        mockMvc.perform(post("/fitnessclass/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createNewFitnessClassFromValidInstructorForOtherInstructor_expect_SuccessResponse() throws Exception {
        Instructor i = new Instructor(12, "Instructor Test", new ArrayList<>(), "null@null.com", "pass", null);
        instructorRepository.save(i);
        Instructor i2 = new Instructor(15, "Other Instructor", new ArrayList<>(), "no@null.com", "pass2", null);
        instructorRepository.save(i2);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        NewFitnessClassDTO n = new NewFitnessClassDTO(15, "Fitness Class 2", 60, 20, LocalDate.now().plusDays(20));
        String expectedJson = ow.writeValueAsString(n);
        mockMvc.perform(post("/fitnessclass/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.class_name").value(n.getClass_name()));
    }

    @Test
    void createNewFitnessClassFromValidInstructorForInvalidInstructorID_expect_EmptyResponse() throws Exception {
        Instructor i = new Instructor(12, "Instructor Test", new ArrayList<>(), "null@null.com", "pass", null);
        instructorRepository.save(i);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        NewFitnessClassDTO n = new NewFitnessClassDTO(100, "Fitness Class 2", 60, 20, LocalDate.now().plusDays(20));
        String expectedJson = ow.writeValueAsString(n);
        mockMvc.perform(post("/fitnessclass/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void createNewFitnessClassFromMemberNotInstructor_expect_AuthorisationResponse() throws Exception {
        Instructor i = new Instructor(12, "Instructor Test", new ArrayList<>(), "null@null.com", "pass", null);
        instructorRepository.save(i);
        Member m = new Member(0, "Test@Person.com", "TestMember", "Barry TestPerson", null, null, "passwordx", null );
        memberRepository.save(m);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        NewFitnessClassDTO n = new NewFitnessClassDTO(12, "Test New Fitness Class", 60, 20, LocalDate.now().plusDays(20));
        String expectedJson = ow.writeValueAsString(n);
        mockMvc.perform(post("/fitnessclass/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isUnauthorized())
                ;
    }

    @Test
    void createNewFitnessClassFromAuthenticatedUserWithInvalidNewFitnessClassObject_expect_BadRequestResponse() throws Exception {
        Instructor i = new Instructor(12, "Instructor Test", new ArrayList<>(), "null@null.com", "pass", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        instructorRepository.save(i);
        NewFitnessClassDTO n = new NewFitnessClassDTO();
        String expectedJson = ow.writeValueAsString(n);
        mockMvc.perform(post("/fitnessclass/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.duration").value("Duration must be more than 0."))
                .andExpect(jsonPath("$.instructor_id").value("Instructor ID must be a valid integer, more than 0."));

    }

    // VIEW FITNESS CLASS USE CASES
    @Test
    void viewFitnessClassAsMemberwithValidFitnessClassID_expect_ValidResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "testPassword1", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>(), "null@null.com", null, null));
        Instructor i = instructorRepository.findById(4).orElse(null);
        if (i != null) {
            FitnessClass f = new FitnessClass(6, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            mockMvc.perform(get("/fitnessclass/6")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", authorisationTokenJSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.class_id").value(6))
                    .andExpect(jsonPath("$.class_instructor.instructor_name").value(i.getInstructor_name()))
                    .andExpect(jsonPath("$.class_name").value(f.getClass_name()));
        }
    }

    @Test
    void viewFitnessClassAsInstructorwithValidFitnessClassID_expect_ValidResponse() throws Exception {
        Instructor i = new Instructor(9, "Instructor 1", new ArrayList<>(), "null@null.com", "password3", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        instructorRepository.save(i);
        i = instructorRepository.findById(9).orElse(null);
        if (i != null) {
            FitnessClass f = new FitnessClass(6, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            mockMvc.perform(get("/fitnessclass/6")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", authorisationTokenJSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.class_id").value(6))
                    .andExpect(jsonPath("$.class_instructor.instructor_name").value(i.getInstructor_name()))
                    .andExpect(jsonPath("$.class_name").value(f.getClass_name()));
        }
    }

    @Test
    void viewFitnessClassAsAuthenticatedwithInvalidClassID_expect_EmptyResponse() throws Exception {
        Instructor i = new Instructor(9, "Instructor 1", new ArrayList<>(), "null@null.com", "password3", null);
        instructorRepository.save(i);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        mockMvc.perform(get("/fitnessclass/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }


    @Test
    void viewFitnessClassAsAuthenticatedwithInvalidCharacter_expect_EmptyResponse() throws Exception {
        Instructor i = new Instructor(9, "Instructor 1", new ArrayList<>(), "null@null.com", "password3", null);
        instructorRepository.save(i);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        mockMvc.perform(get("/fitnessclass/_")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isBadRequest())
                ;
    }

    @Test
    void viewFitnessClassAsUnAuthenticatedWithNonAlphaNumericIDCharacter_expect_AuthorisationResponse() throws Exception {
        mockMvc.perform(get("/fitnessclass/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    // UPDATE FITNESS CLASS USE CASES
    @Test
    void updateFitnessClassWithValidIDandChanges_expectResponse() throws Exception {
        Instructor i = new Instructor(12, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(6, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        i = instructorRepository.findById(12).orElse(null);
        if (i != null) {
            String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
            UpdatedFitnessClassDTO u = new UpdatedFitnessClassDTO(6, f.getUuid(), i.getId(), "Updated Fitness Class", f.getDuration(), f.getSpaces(), f.getBooked_spaces(), f.getClass_date());
            String expectedJson = ow.writeValueAsString(u);
            mockMvc.perform(patch("/fitnessclass/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(expectedJson)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", authorisationTokenJSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.class_id").value(6))
                    .andExpect(jsonPath("$.class_name").value(u.getClass_name()))
                    .andExpect(jsonPath("$.class_instructor.instructor_id").value(i.getId()));
        }
    }

    @Test
    void updateFitnessClassWithValidIDandChangesAsAuthenticatedMember_expectResponse() throws Exception {
        Member m = new Member(10, "test_email_CreateOwner@gmail.com", "Test_User", "Test User", new ArrayList<>(), new ArrayList<>(), "testPassword1", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        memberRepository.save(m);
        Instructor i = new Instructor(12, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(6, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        i = instructorRepository.findById(12).orElse(null);
        if (i != null) {
            UpdatedFitnessClassDTO u = new UpdatedFitnessClassDTO(6, f.getUuid(), i.getId(), "Updated Fitness Class", f.getDuration(), f.getSpaces(), f.getBooked_spaces(), f.getClass_date());
            String expectedJson = ow.writeValueAsString(u);

            mockMvc.perform(patch("/fitnessclass/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(expectedJson)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", authorisationTokenJSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void updateFitnessClassWithValidIDandNoChanges_expectEmptyResponse() throws Exception {
        Instructor i = new Instructor(8, "Instructor 1", new ArrayList<>(), "null@no.com", "null", null);
        instructorRepository.save(i);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        FitnessClass f = new FitnessClass(7, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        UpdatedFitnessClassDTO u = new UpdatedFitnessClassDTO(7, f.getUuid(), i.getId(), f.getClass_name(), f.getDuration(), f.getSpaces(), f.getBooked_spaces(), f.getClass_date());
        String expectedJson = ow.writeValueAsString(u);
        mockMvc.perform(patch("/fitnessclass/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void updateFitnessClassWithInvalidID_expectEmptyResponse() throws Exception {
        Instructor i = new Instructor(8, "Instructor 1", new ArrayList<>(), "null@no.com", "null", null);
        instructorRepository.save(i);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        FitnessClass f = new FitnessClass(8, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        UpdatedFitnessClassDTO u = new UpdatedFitnessClassDTO(88, f.getUuid(), 8, f.getClass_name(), f.getDuration(), f.getSpaces(), f.getBooked_spaces(), f.getClass_date());
        String expectedJson = ow.writeValueAsString(u);
        mockMvc.perform(patch("/fitnessclass/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void updateFitnessClassWithValidIDandChangesAsUnauthenticatedMember_expectResponse() throws Exception {
        Instructor i = new Instructor(12, "Instructor 1", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(6, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);
        i = instructorRepository.findById(12).orElse(null);
        if (i != null) {
            UpdatedFitnessClassDTO u = new UpdatedFitnessClassDTO(6, f.getUuid(), i.getId(), "Updated Fitness Class", f.getDuration(), f.getSpaces(), f.getBooked_spaces(), f.getClass_date());
            String expectedJson = ow.writeValueAsString(u);
            mockMvc.perform(patch("/fitnessclass/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(expectedJson)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    // VIEW FITNESS CLASSES FOR SPECIFIC INSTRUCTOR USE CASES
    @Test
    void viewFitnessClassesForValidInstructorId_expect_Response() throws Exception {
        Instructor i = new Instructor(5, "Instructor One", new ArrayList<>(), "null@null.com", "null", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        instructorRepository.save(i);
        List<FitnessClass> fitnessClassList = new ArrayList<>();
        for (int x = 6; x < 10; x++) {
            FitnessClass tempFitnessClass = new FitnessClass(x,UUID.randomUUID().toString(), "Test Class " + x, 60, 20, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassList.add(tempFitnessClass);
        }
        fitnessClassRepository.saveAll(fitnessClassList);
        i.setClasses(fitnessClassList);
        instructorRepository.save(i);
        mockMvc.perform(get("/fitnessclass/instructor/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructor_id").value(5))
                .andExpect(jsonPath("$.instructor_name").value(i.getInstructor_name()))
                .andExpect(jsonPath("$.instructor_classes[0].class_name").value("Test Class 6"))
                .andExpect(jsonPath("$.instructor_classes[0].class_duration").value(60))
                .andExpect(jsonPath("$.instructor_classes[1].class_name").value("Test Class 7"))
                .andExpect(jsonPath("$.instructor_classes[1].class_booked_spaces").value(0))
                .andExpect(jsonPath("$.instructor_classes[2].class_capacity").value(20));
    }

    @Test
    void viewFitnessClassesForValidInstructorIdFromUnauthenticatedSource_expect_Response() throws Exception {
        Instructor i = new Instructor(5, "Instructor One", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        List<FitnessClass> fitnessClassList = new ArrayList<>();
        for (int x = 6; x < 10; x++) {
            FitnessClass tempFitnessClass = new FitnessClass(x,UUID.randomUUID().toString(), "Test Class " + x, 60, 20, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassList.add(tempFitnessClass);
        }
        fitnessClassRepository.saveAll(fitnessClassList);
        i.setClasses(fitnessClassList);
        instructorRepository.save(i);
        mockMvc.perform(get("/fitnessclass/instructor/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void viewFitnessClassesForValidOtherInstructor_expect_Response() throws Exception {
        Instructor iSearching = new Instructor(3, "Instructor One", new ArrayList<>(), "null3@null.com", "null333", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(iSearching);
        instructorRepository.save(iSearching);
        Instructor i = new Instructor(5, "Instructor One", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        List<FitnessClass> fitnessClassList = new ArrayList<>();
        for (int x = 6; x < 10; x++) {
            FitnessClass tempFitnessClass = new FitnessClass(x,UUID.randomUUID().toString(), "Test Class " + x, 60, 20, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassList.add(tempFitnessClass);
        }
        fitnessClassRepository.saveAll(fitnessClassList);
        i.setClasses(fitnessClassList);
        instructorRepository.save(i);
        mockMvc.perform(get("/fitnessclass/instructor/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructor_id").value(5))
                .andExpect(jsonPath("$.instructor_name").value(i.getInstructor_name()))
                .andExpect(jsonPath("$.instructor_classes[0].class_name").value("Test Class 6"))
                .andExpect(jsonPath("$.instructor_classes[0].class_duration").value(60))
                .andExpect(jsonPath("$.instructor_classes[1].class_name").value("Test Class 7"))
                .andExpect(jsonPath("$.instructor_classes[1].class_booked_spaces").value(0))
                .andExpect(jsonPath("$.instructor_classes[2].class_capacity").value(20));
    }

    @Test
    void viewFitnessClassesforInvalidInstructorId_expect_emptyResponse() throws Exception {
        Instructor i = new Instructor(5, "Instructor One", new ArrayList<>(), "null@null.com", "null", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        instructorRepository.save(i);
        mockMvc.perform(get("/fitnessclass/instructor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void viewFitnessClassesForValidInstructorFromMember_expect_AuthorisationResponse() throws Exception {
        Member m = new Member(0, "Test@Person.com", "TestMember", "Barry TestPerson", null, null, "passwordx", null );
        memberRepository.save(m);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(m);
        Instructor i = new Instructor(5, "Instructor One", new ArrayList<>(), "null@null.com", "null", null);
        instructorRepository.save(i);
        List<FitnessClass> fitnessClassList = new ArrayList<>();
        for (int x = 6; x < 10; x++) {
            FitnessClass tempFitnessClass = new FitnessClass(x,UUID.randomUUID().toString(), "Test Class " + x, 60, 20, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassList.add(tempFitnessClass);
        }
        fitnessClassRepository.saveAll(fitnessClassList);
        i.setClasses(fitnessClassList);
        instructorRepository.save(i);
        mockMvc.perform(get("/fitnessclass/instructor/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void test_viewFitnessClassesForInstructorIdNotAlphaNumeric_expect_BadRequest() throws Exception {
        Instructor i = new Instructor(5, "Instructor One", new ArrayList<>(), "null@null.com", "null", null);
        String authorisationTokenJSON = testUtilityFunctions.generateAuthorisationHeader(i);
        instructorRepository.save(i);
        mockMvc.perform(get("/fitnessclass/instructor/_")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorisationTokenJSON))
                .andExpect(status().isBadRequest());
    }
}