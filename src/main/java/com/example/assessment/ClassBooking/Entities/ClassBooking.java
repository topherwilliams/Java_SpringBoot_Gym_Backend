package com.example.assessment.ClassBooking.Entities;

import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.Member.Entities.Member;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name="class_booking")
public class ClassBooking {

    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name="member_id", nullable=false)
    private Member member;

    @ManyToOne
    @JoinColumn(name="class_id", nullable=false)
    private FitnessClass fitnessClass;
}
