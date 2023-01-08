package com.example.assessment.AuthTokenClass;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AuthTokenClass {
    // Exists to simplify mocking of credentials to include in headers
    String email_address;
    String password;
}
