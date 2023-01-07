package com.example.assessment.ClassBooking;

import com.example.assessment.ClassBooking.DTOs.ClassBooking_FitnessClass_DTO;
import com.example.assessment.ClassBooking.DTOs.NewClassBookingDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/classbooking")
@AllArgsConstructor
@Validated
public class ClassBookingRestController {

    private final ClassBookingService classBookingService;

    @PostMapping(path="/create/{userID}/{classID}")
    ClassBooking_FitnessClass_DTO bookClass(@PathVariable(name = "userID") int userID,
                                            @PathVariable(name = "classID") int classID){
        NewClassBookingDTO n = new NewClassBookingDTO(classID, userID);
        return classBookingService.bookClass(n);
    };

    @DeleteMapping(path="/cancel/{id}")
    void cancelClassBooking(@PathVariable("id")
                            @Min(value=1, message="Please provide a valid member ID")
                            int id) {
        classBookingService.cancelClassBooking(id);
    }

    @GetMapping(path="/attendee/{id}")
    List<ClassBooking_FitnessClass_DTO> getUpcomingClassesByMember(@PathVariable("id")
                                                                   @Min(value=1, message="Please provide a valid member ID")
                                                                   int id) {
        return classBookingService.getUpcomingClassesByMember(id);
    }


}
