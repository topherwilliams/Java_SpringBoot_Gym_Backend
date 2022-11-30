package com.example.assessment.ClassBooking;

import com.example.assessment.ClassBooking.Entities.ClassBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassBookingRepository extends JpaRepository<ClassBooking, Integer> {

    @Modifying
    @Query("DELETE FROM ClassBooking c WHERE c.member.id = :member_id")
    void deleteClassBookingsByMemberID(@Param(value="member_id") int member_id);

    @Query("SELECT c from ClassBooking c WHERE c.member.id = :member_id AND c.fitnessClass.id = :class_id")
    ClassBooking findClassBookingByMemberIDandClassID(@Param(value="member_id") int member_id,
                                                      @Param(value="class_id") int class_id);

    @Query("SELECT c FROM ClassBooking c WHERE c.member.id = :member_id")
    List<ClassBooking> findClassBookingsByMemberID(@Param(value="member_id") int member_id);

}
