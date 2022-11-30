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

    @PostMapping(path="/create")
    ClassBooking_FitnessClass_DTO bookClass(@RequestBody @Valid NewClassBookingDTO newClassBookingDTO){
        return classBookingService.bookClass(newClassBookingDTO);
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
