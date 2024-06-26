package com.example.FootballSimulator.User;

import com.example.FootballSimulator.User.User;
import com.example.FootballSimulator.User.UserRegistrationDTO;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper {
    public User toEntity (UserRegistrationDTO userRegistrationDTO){
        User user = new User();
        user.setName(userRegistrationDTO.getName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setUsername(userRegistrationDTO.getUsername());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(userRegistrationDTO.getPassword());
        return user;
    }
}
