package com.example.assessment.ClassBooking.DTOs;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewClassBookingDTO {

    @NotNull
    @Min(value=1, message="The class ID field must be higher than 0.")
    private int class_id;
    @NotNull
    @Min(value=1, message="The member ID field must be higher than 0.")
    private int member_id;
}
