package com.example.assessment.Filter;

import com.example.assessment.ClassBooking.DTOs.NewClassBookingDTO;
import com.example.assessment.Instructor.DTOs.InstructorCredentialsDTO;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Instructor.InstructorService;
import com.example.assessment.Member.DTOs.MemberCredentialsDTO;
import com.example.assessment.Member.DTOs.UpdatedMemberDTO;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.MemberService;
import com.example.assessment.Workout.DTOs.NewWorkoutDTO;
import com.example.assessment.Workout.Entities.Workout;
import com.example.assessment.Workout.WorkoutRepository;
import com.example.assessment.WorkoutExercise.DTOs.NewWorkoutExerciseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@WebFilter(urlPatterns = {"/*"})
public class AuthorisationFilter implements Filter {

    private final MemberService memberService;
    private final InstructorService instructorService;

    private final WorkoutRepository workoutRepository;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String requestURI = request.getRequestURI().toLowerCase();

        // AUTHORISATION RULES
        if (requestURI.contains("member/create")) {
            // No authorisation required to create a new member
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else if (memberIsAuthorised(request)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
//        else if (instructorIsAuthorised(request)) {
//            filterChain.doFilter(servletRequest, servletResponse);
//        }
        else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean memberIsAuthorised(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String requestURI = request.getRequestURI().toLowerCase();
        String token = request.getHeader("AUTHORIZATION");
        if (token != null) {
            Member m;
            // REMINDER: Convert token to DTO when necessary.
            if (token.contains("email_address") && token.contains("password")) {
                String[] authParts = token.split("\"");
                MemberCredentialsDTO mcredDTO = new MemberCredentialsDTO(authParts[3], authParts[7]);
                m = memberService.checkCredentials(mcredDTO);
            } else {
                m = memberService.checkCredentials(token);
            }
            if (m != null) {
                // Use case authorisation rules for member
                if (requestURI.startsWith("/fitnessclass/create") || requestURI.startsWith("/fitnessclass/update") ||
                        requestURI.startsWith("/fitnessclass/instructor")) {
                    // Members aren't allowed to do these fitnessclass use cases
                    return false;
                } else if (requestURI.startsWith("/classbooking")) {
                    // Members can only book / cancel / view class booking for themselves
                    if (requestURI.startsWith("/classbooking/create")) {
                        // TODO: Authentication happens but request is not passed to endpoint.
                        NewClassBookingDTO nDTO = mapper.readValue(request.getInputStream(), NewClassBookingDTO.class);
                        return m.getId() == nDTO.getMember_id();
                    } else if (requestURI.startsWith("/classbooking/cancel")) {
                        return m.getId() == requestTargetID(requestURI, 2);
                    } else if (requestURI.startsWith("/classbooking/attendee")) {
                        return m.getId() == requestTargetID(requestURI, 2);
                    }
                } else if (requestURI.startsWith("/workout/")) {
                    // Members can only create a workout or add exercises for themselves.
                    if (requestURI.startsWith("/workout/create")) {
                        // TODO: Authentication happens but request is not passed to endpoint.
                        NewWorkoutDTO nwDTO = mapper.readValue(request.getInputStream(), NewWorkoutDTO.class);
                        return m.getId() == nwDTO.getMember_id();
                    } else if (requestURI.startsWith("/workout/addexercise")) {
                        // TODO: Authentication happens but request is not passed to endpoint.
                        NewWorkoutExerciseDTO nEDTO = mapper.readValue(request.getInputStream(), NewWorkoutExerciseDTO.class);
                        Workout w = workoutRepository.findById(nEDTO.getWorkout_id()).orElse(null);
                        if (w != null) {
                            return m.getId() == w.getMember().getId();
                        }
                    }
                } else if (requestURI.startsWith("/member/delete/")) {
                    return m.getId() == requestTargetID(requestURI, 2);
                } else if (requestURI.startsWith("/member/update/")) {
                    // TODO: Authentication happens but request is not passed to endpoint.
                    UpdatedMemberDTO uMemberDTO = mapper.readValue(request.getInputStream(), UpdatedMemberDTO.class);
                    return m.getId() == uMemberDTO.getId();
                }
                else {
                    // All other use cases require no authorisation.
                    return true;
                }
            }
        }
        return false;
    }

    private boolean instructorIsAuthorised(HttpServletRequest request) {
        String requestURI = request.getRequestURI().toLowerCase();
        String token = request.getHeader("AUTHORIZATION");
        if (token != null) {
            Instructor i;
            // REMINDER: Token always received as a string - convert to DTO if necessary.
            if (token.contains("email") && token.contains("password")) {
                String[] authParts = token.split("\"");
                InstructorCredentialsDTO icredDTO = new InstructorCredentialsDTO(authParts[3], authParts[7]);
                i = instructorService.checkCredentials(icredDTO);
            } else {
                i = instructorService.checkCredentials(token);
            }
            if (i != null) {
                // For intended use cases, there are no specific authorisation factors here for the endpoints.
                return true;
            }
        }
        return false;
    }

    private int requestTargetID(String requestURI, int part) {
        String[] parts = requestURI.substring(1).split("/");
        return Integer.parseInt(parts[part]);
    }
}
