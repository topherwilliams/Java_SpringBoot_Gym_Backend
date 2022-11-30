package com.example.assessment.FitnessClass.Entities;

import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.Instructor.Entities.Instructor;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name="fitness_class")
public class FitnessClass {

    @Id
    private int id;
    private String uuid;
    private String class_name;
    private int duration;
    private int spaces;
    private int booked_spaces;
    private LocalDate class_date;

    @ManyToOne
    @JoinColumn(name="instructor_id", nullable = false)
    private Instructor instructor;

    @OneToMany(mappedBy="fitnessClass")
    @OrderBy(value="id")
    private List<ClassBooking> attendees;


}
