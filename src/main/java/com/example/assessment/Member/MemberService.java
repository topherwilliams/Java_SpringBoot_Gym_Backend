package com.example.assessment.Member;

import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.ClassBooking.ClassBookingRepository;
import com.example.assessment.FitnessClass.FitnessClassRepository;
import com.example.assessment.FitnessClass.FitnessClassService;
import com.example.assessment.Member.DTOs.MemberDTO;
import com.example.assessment.Member.DTOs.NewMemberDTO;
import com.example.assessment.Member.DTOs.UpdatedMemberDTO;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.UtilityFunctions.GeneralUtilities;
import com.example.assessment.UtilityFunctions.MemberUtilities;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ClassBookingRepository classBookingRepository;
    private final FitnessClassRepository fitnessClassRepository;

    private final FitnessClassService fitnessClassService;

    List<MemberDTO> getAllMembers() {
        List<MemberDTO> list = new ArrayList<>();
        for (Member m : memberRepository.findAll()) {
            list.add(MemberUtilities.convertMemberToMemberDTO(m));
        };
        return list;
    };

    MemberDTO getMember(int id) {
        Member m = memberRepository.findById(id).orElse(null);
        return m != null? MemberUtilities.convertMemberToMemberDTO(m): null;
        // TODO: Come up with a more elegant response for when no member found...
    };

    MemberDTO createNewMember(NewMemberDTO newMemberDTO) {
        if(!emailAlreadyRegistered(newMemberDTO.getEmail_address())) {
            Member newMember = new Member(0, newMemberDTO.getEmail_address(), newMemberDTO.getUsername(), newMemberDTO.getName(), new ArrayList<>(), new ArrayList<>());
            memberRepository.save(newMember);
            return MemberUtilities.convertMemberToMemberDTO(memberRepository.findMemberByEmailAddress(newMember.getEmail_address()));
        } else {
            // TODO: Come up with a more elegant response for when not able to create new member...
            return null;
        }
    };

    boolean emailAlreadyRegistered(String email_address) {
        return memberRepository.findMemberByEmailAddress(email_address) == null? false : true;
    };

    void deleteMember(int id) {
        if (memberRepository.existsById(id)) {
            Member m = memberRepository.findById(id).orElse(null);
            for (ClassBooking c : m.getClasses()) {
                if (GeneralUtilities.dateInFuture(c.getFitnessClass().getClass_date())) {
                    // for future dated classes: Decrement booked_spaces by one to free up space
                    fitnessClassService.decrementClassBookedSpaces(c.getId());
                }
                // Delete class booking
                classBookingRepository.deleteById(c.getId());
            };
            // Delete member
            memberRepository.deleteById(m.getId());
        }
    };

    MemberDTO updateMember(UpdatedMemberDTO updatedMemberDTO) {
        if (memberRepository.existsById(updatedMemberDTO.getId())) {
            Member memberToUpdate = memberRepository.findById(updatedMemberDTO.getId()).orElse(null);
            UpdatedMemberDTO comparisonExistingMember = new UpdatedMemberDTO(memberToUpdate.getId(), memberToUpdate.getEmail_address(), memberToUpdate.getUsername(), memberToUpdate.getMember_name());
            if(!updatedMemberDTO.equals(comparisonExistingMember)) {
                // Update member details don't match existing - update record
                memberToUpdate.setEmail_address(updatedMemberDTO.getEmail_address());
                memberToUpdate.setUsername(updatedMemberDTO.getUsername());
                memberToUpdate.setMember_name(updatedMemberDTO.getMember_name());
                memberRepository.save(memberToUpdate);
                return MemberUtilities.convertMemberToMemberDTO(memberRepository.findById(updatedMemberDTO.getId()).orElse(null));
            } else { return null; }
        }
        return null;
    };
}
