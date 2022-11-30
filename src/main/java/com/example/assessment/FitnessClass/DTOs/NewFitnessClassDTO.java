package com.example.assessment.FitnessClass.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewFitnessClassDTO {
    @NotNull
    @Min(value=1, message="Instructor ID must be a valid integer, more than 0.")
    private int instructor_id;
    @NotBlank(message="Please provide a proper class name.")
    private String class_name;
    @Min(value=1, message="Duration must be more than 0.")
    private int duration;
    @Min(value=1, message="Spaces must be more than 0.")
    private int spaces;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate class_date;
}
