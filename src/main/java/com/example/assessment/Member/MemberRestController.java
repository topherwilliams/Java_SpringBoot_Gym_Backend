package com.example.assessment.Member;

import com.example.assessment.Member.DTOs.MemberDTO;
import com.example.assessment.Member.DTOs.NewMemberDTO;
import com.example.assessment.Member.DTOs.UpdatedMemberDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path="/member")
@AllArgsConstructor
@Validated
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping
    List<MemberDTO> getAllMembers() {
        return memberService.getAllMembers();
    };

    @GetMapping(path="/{id}")
    MemberDTO getMember(@PathVariable("id")
                        @Min(value=1, message="Please provide a valid member ID")
                        int memberId) {
        return memberService.getMember(memberId);
    };

    @PostMapping( path="/create")
    MemberDTO createNewMember(@RequestBody @Valid NewMemberDTO newMemberDTO) {
        return memberService.createNewMember(newMemberDTO);
    };

    @DeleteMapping(path="delete/{id}")
    void deleteMember(@PathVariable("id")
                      @Min(value=1, message="Please provide a valid member ID")
                      int memberId) {
        memberService.deleteMember(memberId);
    }

    @PatchMapping( path="/update")
    MemberDTO updateMember(@RequestBody UpdatedMemberDTO updatedMemberDTO) {
        System.out.println("Reached update member endpoint...");
        return memberService.updateMember(updatedMemberDTO);
    }

}
