package com.example.assessment.ClassBooking;

import com.example.assessment.ClassBooking.DTOs.ClassBooking_FitnessClass_DTO;
import com.example.assessment.ClassBooking.DTOs.NewClassBookingDTO;
import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.FitnessClass.DTOs.FitnessClass_Shallow_DTO;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.FitnessClass.FitnessClassRepository;
import com.example.assessment.FitnessClass.FitnessClassService;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.MemberRepository;
import com.example.assessment.UtilityFunctions.ClassBookingUtilities;
import com.example.assessment.UtilityFunctions.FitnessClassUtilities;
import com.example.assessment.UtilityFunctions.GeneralUtilities;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ClassBookingService {
    private final ClassBookingRepository classBookingRepository;
    private final FitnessClassRepository fitnessClassRepository;
    private final MemberRepository memberRepository;
    private final FitnessClassService fitnessClassService;

    ClassBooking_FitnessClass_DTO bookClass(NewClassBookingDTO newClassBookingDTO) {
        if(memberRepository.existsById(newClassBookingDTO.getMember_id()) &&
                fitnessClassRepository.existsById(newClassBookingDTO.getClass_id()) &&
                classBookingRepository.findClassBookingByMemberIDandClassID(newClassBookingDTO.getMember_id(), newClassBookingDTO.getClass_id()) == null) {
            // Class and member exist and member hasn't already booked onto class.
            FitnessClass f = fitnessClassRepository.findById(newClassBookingDTO.getClass_id()).orElse(null);
            if (f.getSpaces() - f.getBooked_spaces() < 1 || !GeneralUtilities.dateInFuture(f.getClass_date())) {
                // No spaces available on the class or the class is in the past
                return null;
            } else {
                // Spaces available and valid future class - okay to book.
                Member m = memberRepository.findById(newClassBookingDTO.getMember_id()).orElse(null);
                ClassBooking classBooking = new ClassBooking(0, m, f);
                classBookingRepository.save(classBooking);
                fitnessClassService.incrementClassBookedSpaces(f.getId());
                return ClassBookingUtilities.convertClassBookingtoDTO(classBookingRepository.findClassBookingByMemberIDandClassID(newClassBookingDTO.getMember_id(), newClassBookingDTO.getClass_id()));
            }
        } else {
            // member or class does not exist
            // TODO: More elegant / helpful response possible?
            return null;
        }
    };

    void cancelClassBooking(int id) {
        if(classBookingRepository.existsById(id)) {
            ClassBooking c = classBookingRepository.findById(id).orElse(null);
            classBookingRepository.deleteById(id);
            fitnessClassService.decrementClassBookedSpaces(c.getFitnessClass().getId());
        }
    }

    List<ClassBooking_FitnessClass_DTO> getUpcomingClassesByMember(int id) {
        List<ClassBooking_FitnessClass_DTO> tempDTOList = new ArrayList<>();
        for (ClassBooking c : classBookingRepository.findClassBookingsByMemberID(id)) {
            FitnessClass f = c.getFitnessClass();
            if(GeneralUtilities.dateInFuture(f.getClass_date())) {
                ClassBooking_FitnessClass_DTO tempDTO = ClassBookingUtilities.convertClassBookingtoDTO(c);
                tempDTOList.add(tempDTO);
            }
        }
        return tempDTOList;
    }


}
