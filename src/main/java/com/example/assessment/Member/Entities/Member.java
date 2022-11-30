package com.example.assessment.Member.Entities;

import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.UtilityFunctions.GeneralUtilities;
import com.example.assessment.Workout.Entities.Workout;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name="member")
public class Member {

    @Id
    private int id;
    private String email_address;
    private String username;
    private String member_name;

    @OneToMany(mappedBy = "member")
    @OrderBy(value="id")
    private List<ClassBooking> classes;

    @OneToMany(mappedBy = "member")
    @OrderBy(value="id")
    private List<Workout> workouts;

//    private List<ClassBooking> getUpcomingClasses() {
//        List<ClassBooking> upcomingClasses = new ArrayList<>();
//        for (ClassBooking c : this.classes) {
//            if (GeneralUtilities.dateInFuture(c.getFitnessClass().getClass_date())) {
//                upcomingClasses.add(c);
//            };
//        };
//        return upcomingClasses;
//    };
//
//    private List<ClassBooking> getPriorClasses() {
//        List<ClassBooking> priorClasses = new ArrayList<>();
//        for (ClassBooking c : this.classes) {
//            if(!GeneralUtilities.dateInFuture(c.getFitnessClass().getClass_date())) {
//                priorClasses.add(c);
//            };
//        };
//        return priorClasses;
//    };


};

