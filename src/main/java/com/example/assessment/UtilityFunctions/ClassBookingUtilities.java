package com.example.assessment.UtilityFunctions;

import com.example.assessment.ClassBooking.DTOs.ClassBooking_FitnessClass_DTO;
import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.FitnessClass.DTOs.FitnessClass_Shallow_DTO;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.Instructor.DTOs.InstructorShallowDTO;
import com.example.assessment.Instructor.Entities.Instructor;
import org.springframework.context.annotation.Bean;

public class ClassBookingUtilities {

    public static ClassBooking_FitnessClass_DTO convertClassBookingtoDTO(ClassBooking c) {
        FitnessClass f = c.getFitnessClass();
        Instructor i = f.getInstructor();
        InstructorShallowDTO instructorShallowDTO = new InstructorShallowDTO(i.getId(), i.getInstructor_name());
        FitnessClass_Shallow_DTO class_shallow_dto = new FitnessClass_Shallow_DTO(f.getId(),
                f.getClass_name(), f.getDuration(), f.getSpaces(), f.getBooked_spaces(), f.getClass_date(), instructorShallowDTO);
        return new ClassBooking_FitnessClass_DTO(c.getId(), class_shallow_dto);
    }
}
