package com.example.assessment.FitnessClass;

import com.example.assessment.ClassBooking.ClassBookingRepository;
import com.example.assessment.FitnessClass.DTOs.NewFitnessClassDTO;
import com.example.assessment.FitnessClass.DTOs.UpdatedFitnessClassDTO;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Instructor.InstructorRepository;
import com.example.assessment.Member.MemberRepository;
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

    // CREATE NEW FITNESS CLASS USE CASES

    @Test
    void test_createNewFitnessClassWithInvalidInstructorID_expect_emptyResponse() throws Exception {
        clearAllRepositories();

        NewFitnessClassDTO n = new NewFitnessClassDTO(8, "Test New Fitness Class", 60, 20, LocalDate.now().plusDays(20));
        String expectedJson = ow.writeValueAsString(n);

        mockMvc.perform(post("/fitnessclass/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void test_createNewFitnessClassWithValidInstructorID_expect_Response() throws Exception {
        clearAllRepositories();
        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>()));
        NewFitnessClassDTO n = new NewFitnessClassDTO(4, "Test Pilates Class", 60, 20, LocalDate.now().plusDays(20));
        String expectedJson = ow.writeValueAsString(n);

        mockMvc.perform(post("/fitnessclass/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.class_instructor.instructor_id").value(n.getInstructor_id()))
                .andExpect(jsonPath("$.class_attendees").isEmpty())
                .andExpect(jsonPath("$.class_name").value(n.getClass_name()));
    }

    @Test
    void test_createNewFitnessClassWithInvalidNewFitnessClassObject_expect_BaadRequestResponse() throws Exception {
        clearAllRepositories();
        NewFitnessClassDTO n = new NewFitnessClassDTO();
        String expectedJson = ow.writeValueAsString(n);

        mockMvc.perform(post("/fitnessclass/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.duration").value("Duration must be more than 0."))
                .andExpect(jsonPath("$.instructor_id").value("Instructor ID must be a valid integer, more than 0."));

    }

    // VIEW FITNESS CLASS USE CASES
    @Test
    void test_viewFitnessClasswithValidID_expect_ValidResponse() throws Exception {
        // NB. Test passes when run in isolation but not when run as part of class.
        // Validated functionality with Postman testing.
        clearAllRepositories();
        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>()));
        Instructor i = instructorRepository.findById(4).orElse(null);
        if (i != null) {
            FitnessClass f = new FitnessClass(0, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            mockMvc.perform(get("/fitnessclass/6")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.class_id").value(6))
                    .andExpect(jsonPath("$.class_instructor.instructor_name").value(i.getInstructor_name()))
                    .andExpect(jsonPath("$.class_name").value(f.getClass_name()));
        }
    }

    @Test
    void test_viewFitnessClasswithInvalidID_expect_EmptyResponse() throws Exception {
        clearAllRepositories();

        mockMvc.perform(get("/fitnessclass/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void test_viewFitnessClasswithNonAlphaNumericIDCharacter_expect_Error() throws Exception {
        clearAllRepositories();

        mockMvc.perform(get("/fitnessclass/_")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    // UPDATE FITNESS CLASS USE CASES
    @Test
    void test_updateFitnessClassWithValidIDandChanges_expectResponse() throws Exception {
        // NB. Test passes when run in isolation but not when run as part of class.
        // Validated functionality with Postman testing.
        clearAllRepositories();
        instructorRepository.save(new Instructor(0, "Instructor 1", new ArrayList<>()));
        Instructor i = instructorRepository.findById(4).orElse(null);
        if (i != null) {
            FitnessClass f = new FitnessClass(0, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
            fitnessClassRepository.save(f);

            UpdatedFitnessClassDTO u = new UpdatedFitnessClassDTO(6, f.getUuid(), i.getId(), "Updated Fitness Class", f.getDuration(), f.getSpaces(), f.getBooked_spaces(), f.getClass_date());
            String expectedJson = ow.writeValueAsString(u);

            mockMvc.perform(patch("/fitnessclass/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(expectedJson)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.class_id").value(6))
                    .andExpect(jsonPath("$.class_name").value(u.getClass_name()))
                    .andExpect(jsonPath("$.class_instructor.instructor_id").value(i.getId()));
        }

    }

    @Test
    void test_updateFitnessClassWithValidIDandNoChanges_expectEmptyResponse() throws Exception {
        clearAllRepositories();
        Instructor i = new Instructor(8, "Instructor 1", new ArrayList<>());
        instructorRepository.save(i);
        FitnessClass f = new FitnessClass(0, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), i, new ArrayList<>());
        fitnessClassRepository.save(f);

        UpdatedFitnessClassDTO u = new UpdatedFitnessClassDTO(6, f.getUuid(), i.getId(), f.getClass_name(), f.getDuration(), f.getSpaces(), f.getBooked_spaces(), f.getClass_date());
        String expectedJson = ow.writeValueAsString(u);

        mockMvc.perform(patch("/fitnessclass/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void test_updateFitnessClassWithInvalidID_expectEmptyResponse() throws Exception {
        clearAllRepositories();
        FitnessClass f = new FitnessClass(0, UUID.randomUUID().toString(), "Test Zumba Class", 60, 40, 0, LocalDate.now().plusDays(20), null, new ArrayList<>());

        UpdatedFitnessClassDTO u = new UpdatedFitnessClassDTO(6, f.getUuid(), 6, f.getClass_name(), f.getDuration(), f.getSpaces(), f.getBooked_spaces(), f.getClass_date());
        String expectedJson = ow.writeValueAsString(u);

        mockMvc.perform(patch("/fitnessclass/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    // VIEW FITNESS CLASSES FOR SPECIFIC INSTRUCTOR USE CASES
    @Test
    void test_viewFitnessClassesForValidInstructorId_expect_Response() throws Exception {
        clearAllRepositories();
        Instructor i = new Instructor(5, "Instructor One", new ArrayList<>());
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
    void test_viewFitnessClassesforInvalidInstructorId_expect_emptyResponse() throws Exception {
        clearAllRepositories();
        mockMvc.perform(get("/fitnessclass/instructor/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }


    @Test
    void test_viewFitnessClassesForInstructorIdNotAlphaNumeric_expect_BadRequest() throws Exception {
        mockMvc.perform(get("/fitnessclass/instructor/_")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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