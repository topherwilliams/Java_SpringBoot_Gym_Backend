package com.example.assessment.FitnessClass.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class UpdatedFitnessClassDTO {

    private int id;
    @NotNull
    private String uuid;
    @NotNull
    @Min(value=1, message="Instructor ID must be a valid integer, more than 0.")
    private int instructor_id;
    @NotBlank(message="Please provide a proper class name.")
    private String class_name;
    @Min(value=1, message="Duration must be more than 0.")
    private int duration;
    @Min(value=1, message="Spaces must be more than 0.")
    private int spaces;
    @Min(0)
    private int booked_spaces;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate class_date;
}
