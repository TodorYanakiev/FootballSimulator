package com.example.FootballSimulator.User;

import com.example.FootballSimulator.Constants.Role;
import com.example.FootballSimulator.User.User;
import com.example.FootballSimulator.User.UserRegistrationDTO;
import com.example.FootballSimulator.User.UserRegistrationMapper;
import com.example.FootballSimulator.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRegistrationMapper userRegistrationMapper;
    @Autowired
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public String addUser(@Valid UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
           return "index";
        }
        if (!comparePasswords(userRegistrationDTO.getPassword(),userRegistrationDTO.getRepeatPassword())){
            model.addAttribute("passwordsDoNotMatch","Passwords do not match!");
            return "index";
        }
        User user = userRegistrationMapper.toEntity(userRegistrationDTO);
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        return "registered";
    }
    public boolean comparePasswords(String password,String repeatPassword){
        return password.equals(repeatPassword);
    }

}
