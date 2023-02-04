package com.example.assessment.Filter;

import com.example.assessment.ClassBooking.ClassBookingRepository;
import com.example.assessment.ClassBooking.Entities.ClassBooking;
import com.example.assessment.Instructor.DTOs.InstructorCredentialsDTO;
import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Instructor.InstructorService;
import com.example.assessment.Member.DTOs.MemberCredentialsDTO;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.MemberService;
import com.example.assessment.Workout.Entities.Workout;
import com.example.assessment.Workout.WorkoutRepository;
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
    private final ClassBookingRepository classBookingRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Start of Filter");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setStatus(200);
        String requestURI = request.getRequestURI().toLowerCase();

        // AUTHORISATION RULES
        if (requestURI.contains("member/create")) {
            // No authorisation required to create a new member
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else if (memberIsAuthorised(request)) {
            System.out.println("Member request authenticated, passing to endpoint.");
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else if (instructorIsAuthorised(request)) {
            System.out.println("Instructor request authenticated, passing to endpoint.");
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean memberIsAuthorised(HttpServletRequest request) throws IOException {
        System.out.println("Start of Authentication");
        String requestURI = request.getRequestURI().toLowerCase();
        String token = request.getHeader("AUTHORIZATION");
        if (token != null) {
            Member authenticatedMember;
            if (token.contains("email_address") && token.contains("password")) {
                String[] authParts = token.split("\"");
                MemberCredentialsDTO mcredDTO = new MemberCredentialsDTO(authParts[3], authParts[7]);
                authenticatedMember = memberService.checkCredentials(mcredDTO);
            } else {
                authenticatedMember = memberService.checkCredentials(token);
            }
            if (authenticatedMember != null) {
                if (requestURI.startsWith("/fitnessclass/create") || requestURI.startsWith("/fitnessclass/update") ||
                        requestURI.startsWith("/fitnessclass/instructor")) {
                    return false;
                } else if (requestURI.startsWith("/classbooking/create") || requestURI.startsWith("/classbooking/attendee")) {
                    // TODO Split out attendee due to issues??
                    return authenticatedMember.getId() == requestTargetID(requestURI, 2);
                } else if (requestURI.startsWith("/classbooking/cancel")) {
                    int targetClassBooking = requestTargetID(requestURI, 2);
                    ClassBooking c = classBookingRepository.findById(targetClassBooking).orElse(null);
                    return c != null? authenticatedMember.getId() == c.getMember().getId() : false;
                } else if (requestURI.startsWith("/workout/")) {
                    if (requestURI.startsWith("/workout/create")) {
                        return authenticatedMember.getId() == requestTargetID(requestURI, 2);
                    } else if (requestURI.startsWith("/workout/addexercise")) {
                        int targetWorkoutID = requestTargetID(requestURI, 2);
                        Workout w = workoutRepository.findById(targetWorkoutID).orElse(null);
                        return w != null? authenticatedMember.getId() == w.getMember().getId() : false;
                    }
                } else if (requestURI.startsWith("/member/delete/") || requestURI.startsWith("/member/update/")) {
                    return authenticatedMember.getId() == requestTargetID(requestURI, 2);
                } else { return true; }
            }
        }
        return false;
    };

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
