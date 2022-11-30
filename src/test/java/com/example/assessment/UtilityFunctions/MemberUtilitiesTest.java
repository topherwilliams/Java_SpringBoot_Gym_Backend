package com.example.assessment.UtilityFunctions;

import com.example.assessment.ClassBooking.DTOs.ClassBooking_FitnessClass_DTO;
import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.FitnessClass.DTOs.FitnessClass_Shallow_DTO;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.Instructor.DTOs.InstructorShallowDTO;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Member.DTOs.MemberDTO;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Workout.DTOs.Workout_Shallow_DTO;
import com.example.assessment.Workout.Entities.Workout;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MemberUtilitiesTest {

    @Test
    void test_extractBookedFitnessClassDTOsFromMember_WhereBookingsExist() {
        Random random = new Random();
        int n = random.nextInt(4- 1) + 1;
        Member m = new Member(n, "Test_Member_"+n+"@gmail.com", "Test_User" + n, FitnessClassUtilitiesTest.nameMap.get(n), new ArrayList<>() , new ArrayList<>());
        Instructor in = new Instructor(1, "Instructor " + n, new ArrayList<>());
        List<ClassBooking> classBookingList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            FitnessClass f = new FitnessClass(i, UUID.randomUUID().toString(), FitnessClassUtilitiesTest.classNameMap.get(n), 45, 20, n+10, LocalDate.now().plusDays(30), in, new ArrayList<>());
            ClassBooking c = new ClassBooking(i, m, f);
            classBookingList.add(c);
        }
        m.setClasses(classBookingList);
        List<ClassBooking_FitnessClass_DTO> comparisonDTO = MemberUtilities.extractBookedFitnessClassDTOsFromMember(m.getClasses());
        assertTrue(comparisonDTO instanceof List<ClassBooking_FitnessClass_DTO>);
        assertEquals(4, comparisonDTO.size());
        assertEquals(1, comparisonDTO.get(0).getBooking_id());
        assertEquals(2, comparisonDTO.get(1).getClass_information().getClass_id());
        assertEquals(LocalDate.now().plusDays(30), comparisonDTO.get(3).getClass_information().getClass_date());
    }

    @Test
    void test_extractBookedFitnessClassDTOsFromMember_WhereNoBookingsExist() {
        Random random = new Random();
        int n = random.nextInt(4- 1) + 1;
        List<ClassBooking> classBookingList = new ArrayList<>();
        Member m = new Member(n, "Test_Member_"+n+"@gmail.com", "Test_User" + n, FitnessClassUtilitiesTest.nameMap.get(n), classBookingList, new ArrayList<>());
        List<ClassBooking_FitnessClass_DTO> comparisonDTO = MemberUtilities.extractBookedFitnessClassDTOsFromMember(m.getClasses());
        assertEquals(0, comparisonDTO.size());
        assertTrue(comparisonDTO instanceof List<ClassBooking_FitnessClass_DTO>);

    }

    @Test
    void test_extractWorkoutsFromMember_WhereWorkoutsExist() {
        Random random = new Random();
        int n = random.nextInt(4- 1) + 1;
        Member m = new Member(n, "Test_Member_"+n+"@gmail.com", "Test_User" + n, FitnessClassUtilitiesTest.nameMap.get(n), new ArrayList<>(), new ArrayList<>());
        List<Workout> workoutList = new ArrayList<>();
        for(int i = 1; i < 5; i++) {
            Workout w = new Workout(i, UUID.randomUUID().toString(), m, new ArrayList<>());
            workoutList.add(w);
        }
        m.setWorkouts(workoutList);
        List<Workout_Shallow_DTO> comparisonDTO = MemberUtilities.extractWorkoutsfromMember(m.getWorkouts());
        assertEquals(4, comparisonDTO.size());
        assertTrue(comparisonDTO instanceof List<Workout_Shallow_DTO>);
        assertEquals(1, comparisonDTO.get(0).getWorkout_id());
        assertEquals(4, comparisonDTO.get(3).getWorkout_id());
    }

    @Test
    void test_extractWorkoutsFromMember_WhereNoWorkoutsExist() {
        Random random = new Random();
        int n = random.nextInt(4- 1) + 1;
        Member m = new Member(n, "Test_Member_"+n+"@gmail.com", "Test_User" + n, FitnessClassUtilitiesTest.nameMap.get(n), new ArrayList<>(), new ArrayList<>());
        List<Workout_Shallow_DTO> comparisonDTO = MemberUtilities.extractWorkoutsfromMember(m.getWorkouts());
        assertEquals(0, comparisonDTO.size());
        assertTrue(comparisonDTO instanceof List<Workout_Shallow_DTO>);
        List<Workout> workoutList = new ArrayList<>();
        for(int i = 1; i < 5; i++) {
            Workout w = new Workout(i, UUID.randomUUID().toString(), m, new ArrayList<>());
            workoutList.add(w);
        }
        m.setWorkouts(workoutList);
    }

    @Test
    void test_convertInvalidMemberToDTO() {
        Member m = new Member();
        assertThrows(NullPointerException.class, () -> {
            MemberDTO comparisonDTO = MemberUtilities.convertMemberToMemberDTO(m);
        });
    }

    @Test
    void test_convertValidMemberWithNoWorkoutsorClassesToDTO() {
        Random random = new Random();
        int n = random.nextInt(4- 1) + 1;
        Member m = new Member(n, "Test_Member_"+n+"@gmail.com", "Test_User" + n, FitnessClassUtilitiesTest.nameMap.get(n), new ArrayList<>(), new ArrayList<>());
        MemberDTO comparisonDTO = MemberUtilities.convertMemberToMemberDTO(m);
        assertTrue(comparisonDTO instanceof MemberDTO);
        assertEquals(n, comparisonDTO.getMember_id());
        assertEquals(FitnessClassUtilitiesTest.nameMap.get(n), comparisonDTO.getMember_name());
        assertEquals(0, comparisonDTO.getMember_workouts().size());
        assertEquals(0, comparisonDTO.getMember_booked_classes().size());
    }

    @Test
    void test_convertValidMemberWithWorkoutsToDTO() {
        Random random = new Random();
        int n = random.nextInt(4- 1) + 1;
        Member m = new Member(n, "Test_Member_"+n+"@gmail.com", "Test_User" + n, FitnessClassUtilitiesTest.nameMap.get(n), new ArrayList<>(), new ArrayList<>());
        // Add workouts to Member and test
        List<Workout> workoutList = new ArrayList<>();
        for(int i = 1; i < 5; i++) {
            Workout w = new Workout(i, UUID.randomUUID().toString(), m, new ArrayList<>());
            workoutList.add(w);
        }
        m.setWorkouts(workoutList);
        MemberDTO comparisonDTO = MemberUtilities.convertMemberToMemberDTO(m);
        assertTrue(comparisonDTO instanceof MemberDTO);
        assertEquals(n, comparisonDTO.getMember_id());
        assertEquals(FitnessClassUtilitiesTest.nameMap.get(n), comparisonDTO.getMember_name());
        assertEquals(4, comparisonDTO.getMember_workouts().size());
        assertEquals(4, comparisonDTO.getMember_workouts().get(3).getWorkout_id());
        assertEquals(0, comparisonDTO.getMember_booked_classes().size());
    }

    @Test
    void test_convertValidMemberWithClassesToDTO() {
        Random random = new Random();
        int n = random.nextInt(4- 1) + 1;
        Member m = new Member(n, "Test_Member_"+n+"@gmail.com", "Test_User" + n, FitnessClassUtilitiesTest.nameMap.get(n), new ArrayList<>(), new ArrayList<>());
        Instructor in = new Instructor(1, "Instructor 1", new ArrayList<>());
        // Add workouts to Member and test
        List<ClassBooking> classBookingList = new ArrayList<>();
        for(int i = 1; i < 5; i++) {
            FitnessClass f = new FitnessClass(i, UUID.randomUUID().toString(), FitnessClassUtilitiesTest.classNameMap.get(i), 45, 20, 5, LocalDate.now().plusDays(20), in, new ArrayList<>());
            ClassBooking c = new ClassBooking(i, m, f);
            classBookingList.add(c);
        }
        m.setClasses(classBookingList);
        MemberDTO comparisonDTO = MemberUtilities.convertMemberToMemberDTO(m);
        assertTrue(comparisonDTO instanceof MemberDTO);
        assertEquals(n, comparisonDTO.getMember_id());
        assertEquals(FitnessClassUtilitiesTest.nameMap.get(n), comparisonDTO.getMember_name());
        assertEquals(4, comparisonDTO.getMember_booked_classes().size());
        assertEquals(4, comparisonDTO.getMember_booked_classes().get(3).getBooking_id());
        assertEquals(0, comparisonDTO.getMember_workouts().size());
    }


}