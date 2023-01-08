package com.example.assessment.ClassBooking.DTOs;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IncomingClassBookingDTO {
    @NotNull
    @Min(value=1, message="The class ID field must be higher than 0.")
    private int class_id;
}
