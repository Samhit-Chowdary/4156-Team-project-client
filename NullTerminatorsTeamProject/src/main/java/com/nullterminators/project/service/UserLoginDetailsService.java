package com.nullterminators.project.service;

import com.nullterminators.project.model.UserLoginDetails;
import com.nullterminators.project.repository.UserLoginDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserLoginDetailsService implements UserDetailsService {

    @Autowired
    private UserLoginDetailsRepository userLoginDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserLoginDetails> user = userLoginDetailsRepository.findByUsername(username);
        if (user.isPresent()) {
            var userObject = user.get();
            return User.builder()
                    .username(userObject.getUsername())
                    .password(userObject.getPassword())
                    .roles(userObject.getRole())
                    .build();
        } else {
            throw new UsernameNotFoundException("User with name: " + username + " not found");
        }
    }

    public String createUser(Integer empId, String username, String password, String designation) {
        String role = switch (designation.toLowerCase()) {
            case "it" -> "SUPERUSER";
            case "doctor" -> "DOCTOR";
            case "nurse" -> "NURSE";
            default -> "OTHER";
        };

        UserLoginDetails userLoginDetails = new UserLoginDetails();
        userLoginDetails.setEmployeeId(empId);
        userLoginDetails.setUsername(username);
        userLoginDetails.setPassword(new BCryptPasswordEncoder().encode(password));
        userLoginDetails.setRole(role);
        try{
            userLoginDetailsRepository.save(userLoginDetails);
            return "";
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
}
