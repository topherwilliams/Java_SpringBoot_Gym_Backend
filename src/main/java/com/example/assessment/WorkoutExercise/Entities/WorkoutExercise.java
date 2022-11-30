package com.example.assessment.WorkoutExercise.Entities;

import com.example.assessment.Workout.Entities.Workout;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name="workout_exercise")
public class WorkoutExercise {
    @Id
    private int id;
    private String exercise_name;
    private int weight_kg;
    private int reps;
    private int exercise_sets;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable=false)
    private Workout workout;

}
