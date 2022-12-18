package com.example.assessment.Instructor.Entities;

import com.example.assessment.FitnessClass.Entities.FitnessClass;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name="instructor")
public class Instructor {

    @Id
    private int id;

    private String instructor_name;

    @OneToMany(mappedBy = "instructor")
    @OrderBy(value="id")
    private List<FitnessClass> classes;
    private String email;
    private String password;
    private String token;

}
