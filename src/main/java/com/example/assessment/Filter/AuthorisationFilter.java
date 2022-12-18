package com.example.assessment.Filter;

import com.example.assessment.Instructor.Entities.Instructor;
import com.example.assessment.Instructor.InstructorService;
import com.example.assessment.Member.DTOs.MemberCredentialsDTO;
import com.example.assessment.Member.DTOs.UpdatedMemberDTO;
import com.example.assessment.Member.Entities.Member;
import com.example.assessment.Member.MemberService;
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


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        TODO: Currently doing basic work to get authentication and authorisation working. Once working revisit UML diagrams and come up with proper rules.
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String requestURI = request.getRequestURI().toLowerCase();

        // Authorisation Rules
        if (requestURI.contains("member/create")) {
            // No authorisation required
            System.out.println("No auth required");
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else if (memberIsAuthorised(request)) {
            System.out.println("Member is authorised. Allow through.");
            System.out.println(request.getRequestURI());
            //TODO: This is not working for update Member endpoint. Authorises okay but never seems to reach the endpoint after auth. All other member use cases are reaching endpoints.
            filterChain.doFilter(servletRequest, servletResponse);
        }
//        else if (instructorIsAuthorised(request)) {
//            System.out.println("Instructor is authorised. Allow through.");
//            filterChain.doFilter(servletRequest, servletResponse);
//        }
        else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean memberIsAuthorised(HttpServletRequest request) throws IOException {
        System.out.println("Member authorisation");
        String requestURI = request.getRequestURI().toLowerCase();
        String token = request.getHeader("AUTHORIZATION");
        Member m;
        // REMINDER: Token always received as a string - convert to DTO if necessary.
        if (token.contains("email_address") && token.contains("password")) {
            String[] authParts = token.split("\"");
            MemberCredentialsDTO mcredDTO = new MemberCredentialsDTO(authParts[3], authParts[7]);
            m = memberService.checkCredentials(mcredDTO);
        } else {
            m = memberService.checkCredentials(token);
        }
        if (m != null) {
            if (requestURI.startsWith("/member/delete/")) {
                //System.out.println("Delete / update use case - additional validation required");
                String[] parts = requestURI.substring(1).split("/");
                int id = Integer.parseInt(parts[2]);
                return m.getId() == id;
            } else if (requestURI.startsWith("/member/update/")) {
                ObjectMapper mapper = new ObjectMapper();
                UpdatedMemberDTO uMemberDTO = mapper.readValue(request.getInputStream(), UpdatedMemberDTO.class);
                // compare ID of user who submitted request (i.e. in Header/Authorization) with the ID in the body of the request (intended target).
                return m.getId() == uMemberDTO.getId();
            }
            else {
                return true;
            }
        }
        return false;
    }

    private boolean instructorIsAuthorised(HttpServletRequest request) {
        String requestURI = request.getRequestURI().toLowerCase();
        String token = request.getHeader("AUTHORIZATION");
        Instructor i = instructorService.checkCredentials(token);

        if (i != null) {
            // For intended use cases, there are no specific authorisation factors here for the endpoints.
            return true;
        }
        return false;
    }

}
