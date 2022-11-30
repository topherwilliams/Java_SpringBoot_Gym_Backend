package com.example.assessment.Workout.Entities;

import com.example.assessment.Member.Entities.Member;
import com.example.assessment.WorkoutExercise.Entities.WorkoutExercise;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name="workout")
public class Workout {

    @Id
    private int id;
    private String uuid;

    @ManyToOne
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "workout")
    @OrderBy(value="id")
    private List<WorkoutExercise> exercises;

}
