package com.example.assessment.UtilityFunctions;

import com.example.assessment.ClassBooking.DTOs.ClassBooking_Member_DTO;
import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.FitnessClass.DTOs.FitnessClass_All_DTO;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Member.Entities.Member;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FitnessClassUtilitiesTest {

    static HashMap<Integer, String> nameMap = new HashMap<>() {{
        put(1, "Bob Test");
        put(2, "James Test");
        put(3, "Sally Test");
        put(4, "Nicola Test");
    }};
    static HashMap<Integer, String> classNameMap = new HashMap<>() {{
        put(1, "Test Yoga Class");
        put(2, "Test Pilates Class");
        put(3, "Test Zumba Class");
        put(4, "Test Spin Class");
    }};

    @Test
    void test_extractAttendeesFromFitnessClassesWhereAttendeesExist() {
        List<ClassBooking> l = new ArrayList<>();
        for (int n = 1; n < 5; n++) {
            Member m = new Member(n, "testEmail" + n + "@gmail.com", "TestUser"+n,  nameMap.get(n), new ArrayList<>(), new ArrayList<>(), null, null);
            ClassBooking c = createClassBookingObject(n, m);
            l.add(c);
        }
        List<ClassBooking_Member_DTO> comparisonDTO = FitnessClassUtilities.extractAttendeesFromFitnessClass(l);
        assertEquals(4, comparisonDTO.size());
        assertEquals(nameMap.get(2), comparisonDTO.get(1).getMember_information().getMember_name());
        assertEquals(nameMap.get(4), comparisonDTO.get(3).getMember_information().getMember_name());
        assertEquals(1, comparisonDTO.get(0).getBooking_id());
        assertEquals(3, comparisonDTO.get(2).getBooking_id());
    }

    @Test
    void test_extractAttendeesFromFitnessClassesWhereNoAttendeesExist() {
        List<ClassBooking> l = new ArrayList<>();
        List<ClassBooking_Member_DTO> comparisonDTO = FitnessClassUtilities.extractAttendeesFromFitnessClass(l);
        assertEquals(0, comparisonDTO.size());
    }

    @Test
    void test_testConvertFitnessClassToDTO() {
        Random random = new Random();
        int n = random.nextInt(4- 1) + 1;
        Member m = new Member(n, "testEmail" + n + "@gmail.com", "TestUser"+n,  nameMap.get(n), new ArrayList<>(), new ArrayList<>(), null, null);
        FitnessClass f = createFitnessClassObject(n, m);
        FitnessClass_All_DTO comparisonFitnessClassDTO = FitnessClassUtilities.convertFitnessClassToDTO(f);
        assertEquals(n, comparisonFitnessClassDTO.getClass_id());
        assertEquals(f.getInstructor().getInstructor_name(), comparisonFitnessClassDTO.getClass_instructor().getInstructor_name());
        assertEquals(f.getClass_date(), comparisonFitnessClassDTO.getClass_date());
        assertEquals(f.getBooked_spaces(), comparisonFitnessClassDTO.getClass_booked_spaces());
    }

    FitnessClass createFitnessClassObject(int n, Member m) {
        Instructor i = new Instructor(n, "Test Instructor "+n, new ArrayList<>(), null, null, null);
        return new FitnessClass(n, UUID.randomUUID().toString(), classNameMap.get(n), 60, 20, n, LocalDate.parse("2022-11-0"+n), i, new ArrayList<>());
    }

    ClassBooking createClassBookingObject(int n, Member m) {
        FitnessClass c = createFitnessClassObject(n, m);
        return new ClassBooking(n, m, c);
    }
}