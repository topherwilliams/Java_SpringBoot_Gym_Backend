package com.example.assessment.FitnessClass;

import com.example.assessment.FitnessClass.DTOs.FitnessClass_All_DTO;
import com.example.assessment.FitnessClass.DTOs.NewFitnessClassDTO;
import com.example.assessment.FitnessClass.DTOs.UpdatedFitnessClassDTO;
import com.example.assessment.Instructor.DTOs.InstructorDeepDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path="/fitnessclass")
@AllArgsConstructor
@Validated
public class FitnessClassRestController {

    private final FitnessClassService fitnessClassService;

    @GetMapping
    List<FitnessClass_All_DTO> getAllFitnessClasses() {
        return fitnessClassService.getAllFitnessClasses();
    };

    @GetMapping(path="/{id}")
    FitnessClass_All_DTO getFitnessClass(@PathVariable("id")
                                         @Min(value=1, message="Please provide a valid member ID")
                                         int classId) {
        return fitnessClassService.getFitnessClass(classId);
    };

    @PostMapping(path="/create")
    FitnessClass_All_DTO createNewFitnessClass(@RequestBody @Valid NewFitnessClassDTO newFitnessClassDTO) {
        return fitnessClassService.createNewFitnessClass(newFitnessClassDTO);
    };

    @PatchMapping(path="/update")
    FitnessClass_All_DTO updateFitnessClass(@RequestBody @Valid UpdatedFitnessClassDTO updatedFitnessClassDTO) {
        return fitnessClassService.updateFitnessClass(updatedFitnessClassDTO);
    }

    @GetMapping(path="/instructor/{id}")
    InstructorDeepDTO getFitnessClassesByInstructorId(@PathVariable("id")
                                                      @Min(value=1, message="Please provide a valid member ID")
                                                      int instructor_id) {
        return fitnessClassService.getUpcomingClassesForInstructor(instructor_id);
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

    //TODO - Check this one as new.
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationExceptions (ConstraintViolationException ex) {
        String violations = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getMessageTemplate())
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>("Validation Error: " + violations, HttpStatus.BAD_REQUEST);
    }
}
