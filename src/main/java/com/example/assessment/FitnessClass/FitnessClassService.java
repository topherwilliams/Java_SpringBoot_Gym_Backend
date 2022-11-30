package com.example.assessment.FitnessClass;

import com.example.assessment.ClassBooking.DTOs.ClassBooking_Member_DTO;
import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.FitnessClass.DTOs.FitnessClass_All_DTO;
import com.example.assessment.FitnessClass.DTOs.FitnessClass_NoInstructor_DTO;
import com.example.assessment.FitnessClass.DTOs.NewFitnessClassDTO;
import com.example.assessment.FitnessClass.DTOs.UpdatedFitnessClassDTO;
import com.example.assessment.FitnessClass.Entities.FitnessClass;
import com.example.assessment.Instructor.DTOs.InstructorDeepDTO;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Instructor.InstructorRepository;
import com.example.assessment.Member.DTOs.Member_Shallow_DTO;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.UtilityFunctions.FitnessClassUtilities;
import com.example.assessment.UtilityFunctions.GeneralUtilities;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class FitnessClassService {

    private final FitnessClassRepository fitnessClassRepository;
    private final InstructorRepository instructorRepository;

    FitnessClass_All_DTO createNewFitnessClass(NewFitnessClassDTO n) {
        if (instructorRepository.existsById(n.getInstructor_id())) {
            Instructor i = instructorRepository.findById(n.getInstructor_id()).orElse(null);
            FitnessClass f = new FitnessClass(0, UUID.randomUUID().toString(), n.getClass_name(), n.getDuration(), n.getSpaces(), 0, n.getClass_date(), i, new ArrayList<>());
            fitnessClassRepository.save(f);
            return FitnessClassUtilities.convertFitnessClassToDTO(fitnessClassRepository.findFitnessClassByUuidEquals(f.getUuid()));
        }
        return null;
    };

    List<FitnessClass_All_DTO> getAllFitnessClasses() {
        List<FitnessClass_All_DTO> fitnessClassListDTOs = new ArrayList<>();
        for(FitnessClass f : fitnessClassRepository.findAll()) {
            fitnessClassListDTOs.add(FitnessClassUtilities.convertFitnessClassToDTO(f));
        }
        return fitnessClassListDTOs;
    }

    FitnessClass_All_DTO getFitnessClass(int classId) {
        FitnessClass f = fitnessClassRepository.findById(classId).orElse(null);
        if (f!=null) {
            return FitnessClassUtilities.convertFitnessClassToDTO(f);
        }
        return null;
    };

    FitnessClass_All_DTO updateFitnessClass(UpdatedFitnessClassDTO updatedFitnessClassDTO) {
        if (fitnessClassRepository.existsById(updatedFitnessClassDTO.getId())) {
            FitnessClass fitnessClassToUpdate = fitnessClassRepository.findById(updatedFitnessClassDTO.getId()).orElse(null);
            UpdatedFitnessClassDTO comparisonExistingFitnessClass = new UpdatedFitnessClassDTO(fitnessClassToUpdate.getId(),
                    fitnessClassToUpdate.getUuid(),
                    fitnessClassToUpdate.getInstructor().getId(),
                    fitnessClassToUpdate.getClass_name(),
                    fitnessClassToUpdate.getDuration(),
                    fitnessClassToUpdate.getSpaces(),
                    fitnessClassToUpdate.getBooked_spaces(),
                    fitnessClassToUpdate.getClass_date()
                    );
            if(!updatedFitnessClassDTO.equals(comparisonExistingFitnessClass)) {
                // Update member details don't match existing - update record
                if (fitnessClassToUpdate.getInstructor().getId() != updatedFitnessClassDTO.getInstructor_id()) {
                    Instructor i = instructorRepository.findById(updatedFitnessClassDTO.getInstructor_id()).orElse(null);
                    if (i != null) {
                        fitnessClassToUpdate.setInstructor(i);
                    } else {
                        return null;
                    }
                }
                fitnessClassToUpdate.setClass_name(updatedFitnessClassDTO.getClass_name());
                fitnessClassToUpdate.setDuration(updatedFitnessClassDTO.getDuration());
                fitnessClassToUpdate.setSpaces(updatedFitnessClassDTO.getSpaces());
                fitnessClassToUpdate.setBooked_spaces(updatedFitnessClassDTO.getBooked_spaces());
                // In reality - would you allow the request to change booked spaces when this is driven by bookings
                fitnessClassToUpdate.setClass_date(updatedFitnessClassDTO.getClass_date());
                fitnessClassRepository.save(fitnessClassToUpdate);
                return FitnessClassUtilities.convertFitnessClassToDTO(fitnessClassRepository.findById(updatedFitnessClassDTO.getId()).orElse(null));
            } else { return null; }
        }
        return null;
    }

    public InstructorDeepDTO getUpcomingClassesForInstructor(int instructor_id) {
        if(instructorRepository.existsById(instructor_id)) {
            Instructor i = instructorRepository.findById(instructor_id).orElse(null);
            List<FitnessClass_NoInstructor_DTO> tempDTOList = new ArrayList<>();
            for(FitnessClass c : i.getClasses()) {
                if(GeneralUtilities.dateInFuture(c.getClass_date())) {
                    List<ClassBooking_Member_DTO> attendeeDTOList = new ArrayList<>();
                    for(ClassBooking cb : c.getAttendees()) {
                        Member m = cb.getMember();
                        ClassBooking_Member_DTO tempAttendeeDTO = new ClassBooking_Member_DTO(cb.getId(),
                                new Member_Shallow_DTO(m.getId(), m.getEmail_address(), m.getUsername() ,m.getMember_name()));
                        attendeeDTOList.add(tempAttendeeDTO);
                    }
                    FitnessClass_NoInstructor_DTO tempDTO = new FitnessClass_NoInstructor_DTO(c.getId(),
                            c.getClass_name(),
                            c.getDuration(),
                            c.getSpaces(),
                            c.getBooked_spaces(),
                            c.getClass_date(),
                            attendeeDTOList
                            );
                    tempDTOList.add(tempDTO);
                }
            }
            return new InstructorDeepDTO(i.getId(), i.getInstructor_name(), tempDTOList);
        } else {
            return null;
        }
    }

    public void decrementClassBookedSpaces(int classId) {
        FitnessClass f = fitnessClassRepository.findById(classId).orElse(null);
        if (f!=null) {
            f.setBooked_spaces(f.getBooked_spaces()-1);
            fitnessClassRepository.save(f);
        }
    };

    public void incrementClassBookedSpaces(int classId) {
        FitnessClass f = fitnessClassRepository.findById(classId).orElse(null);
        if (f!=null) {
            f.setBooked_spaces(f.getBooked_spaces()+1);
            fitnessClassRepository.save(f);
        }
    };

}
