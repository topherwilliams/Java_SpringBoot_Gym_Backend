package com.example.assessment.Member;

import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.User.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

//    Bespoke repository functions here

    @Query("SELECT m FROM Member m WHERE m.email_address = :email_address")
    Member findMemberByEmailAddress(
            @Param(value="email_address") String email_address);


    Member findByToken(String token);
//
//    Member findByEmail_address(String email_address);

}
