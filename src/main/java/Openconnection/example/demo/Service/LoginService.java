package Openconnection.example.demo.Service;

import Openconnection.example.demo.Repository.UserDetailsRepository;
import Openconnection.example.demo.beans.UserDetails;
import Openconnection.example.demo.beans.UserType;
import Openconnection.example.demo.utills.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserDetailsRepository userDetailsRepository;
    private final Jwt jwt;


    public String login(String email, String password) {
        // Retrieve user details by email
        UserDetails user = userDetailsRepository.findByEmail(email);

        // If user exists and password matches
        if (user != null && user.getPassword().equals(password)) {
            // Generate JWT token for the user
            return jwt.generateToken(user);
        }

        // Return null if login fails
        return null;
    }

    public boolean validateToken(String token) {
        try {
            return jwt.validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkUser(String token, UserType userType) {
        try {
            return jwt.checkUser(token, userType);
        } catch (Exception e) {
            return false;
        }
    }
}