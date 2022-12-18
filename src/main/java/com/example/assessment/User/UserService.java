package com.example.assessment.User;

import com.example.assessment.User.DTOs.UserCredentialsDTO;
import com.example.assessment.User.Entities.User;
import com.example.assessment.UtilityFunctions.StringHasher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StringHasher stringHasher;

    public void clearToken(int userID) {
        User u = userRepository.findById(userID).orElse(null);
        if (u != null) {
            u.setToken(null);
            userRepository.save(u);
        }
    }

    public User checkCredentials(UserCredentialsDTO credentials) {
        User u = userRepository.findByEmail(credentials.getEmail());
        if (u != null && u.getPassword().equals(credentials.getPassword())) {
            String token = stringHasher.hashString(u.getEmail() + ":" + LocalDate.now().toString());
            u.setToken(token);
            userRepository.save(u);
            return u;
        }
        return null;
    }

    public User checkCredentials(String token) {
        User u = userRepository.findByToken(token);
        if (u != null && u.getToken() != null) {
            return u;
        }
        return null;
    }

}
