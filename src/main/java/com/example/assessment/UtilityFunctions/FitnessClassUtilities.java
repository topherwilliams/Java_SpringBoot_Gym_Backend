package com.example.assessment.UtilityFunctions;

import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.ClassBooking.DTOs.ClassBooking_Member_DTO;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.FitnessClass.DTOs.FitnessClass_All_DTO;
import com.example.assessment.Instructor.DTOs.InstructorShallowDTO;
import com.example.assessment.Member.DTOs.Member_Shallow_DTO;

import java.util.ArrayList;
import java.util.List;

public class FitnessClassUtilities {

    public static List<ClassBooking_Member_DTO> extractAttendeesFromFitnessClass(List<ClassBooking> classBookingList) {
        List<ClassBooking_Member_DTO> attendees = new ArrayList<>();
        for (ClassBooking c: classBookingList) {
            Member_Shallow_DTO member_shallow_dto = new Member_Shallow_DTO(c.getMember().getId(), c.getMember().getEmail_address(), c.getMember().getUsername(), c.getMember().getMember_name());
            ClassBooking_Member_DTO memberClassDTO = new ClassBooking_Member_DTO(c.getId(), member_shallow_dto);
            attendees.add(memberClassDTO);
        }
        return attendees;
    };

    public static FitnessClass_All_DTO convertFitnessClassToDTO(FitnessClass f) {
        List<ClassBooking_Member_DTO> tempAttendees = extractAttendeesFromFitnessClass(f.getAttendees());
        InstructorShallowDTO i = new InstructorShallowDTO(f.getInstructor().getId(), f.getInstructor().getInstructor_name());
        return new FitnessClass_All_DTO(f.getId(), f.getClass_name(), f.getDuration(), f.getSpaces(), f.getBooked_spaces(), f.getClass_date(), i, tempAttendees);
    }



}
