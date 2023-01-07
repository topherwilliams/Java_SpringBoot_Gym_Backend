package com.example.assessment.UtilityFunctions;

import com.example.assessment.ClassBooking.DTOs.ClassBooking_FitnessClass_DTO;
import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Member.Entities.Member;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClassBookingUtilitiesTest {

    @Test
    public void test_ConvertClassBookingtoDTO() {
        Member m = new Member(1, "testEmail@gmail.com", "testUser1", "Bob Test", new ArrayList<>(), new ArrayList<>(), null, null);
        Instructor i = new Instructor(1, "Test Instructor 1", new ArrayList<>(), null, null, null);
        FitnessClass f = new FitnessClass(1, "44a2cae0-58b3-431e-a054-c48b7cd38062", "Test Yoga Class", 60, 20, 1, LocalDate.parse("2022-11-30"), i, new ArrayList<>());
        ClassBooking c = new ClassBooking(1, m, f);
        ClassBooking_FitnessClass_DTO comparisonDTO = ClassBookingUtilities.convertClassBookingtoDTO(c);
        assertEquals(i.getInstructor_name(), comparisonDTO.getClass_information().getClass_instructor().getInstructor_name());
        assertEquals(f.getClass_name(), comparisonDTO.getClass_information().getClass_name());
    };


}