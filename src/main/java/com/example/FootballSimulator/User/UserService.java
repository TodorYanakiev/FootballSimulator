package com.example.FootballSimulator.User;

import com.example.FootballSimulator.Constants.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private Map<String, User> userDatabase;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRegistrationMapper userRegistrationMapper;
    @Autowired
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public String login(){
        return "/user/login";
    }
    public String register(Model model){
        model.addAttribute("userRegistrationDTO",new UserRegistrationDTO());
        return "/user/registration";
    }
    public String addUser(@Valid UserRegistrationDTO userRegistrationDTO,BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
           return "/user/registration";
        }
        if (!comparePasswords(userRegistrationDTO.getPassword(),userRegistrationDTO.getRepeatPassword())){
            model.addAttribute("passwordsDoNotMatch","Passwords do not match!");
            return "/user/registration";
        }
        User user = userRegistrationMapper.toEntity(userRegistrationDTO);
        List<Object> userPresentObj = isUserPresent(user);
        if((Boolean) userPresentObj.get(0)){

            return "/user/registration";
        }
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        return "redirect:/auth/login";
    }
public List<Object> isUserPresent(User user) {
    boolean userExists = false;
    String message = null;
    User u  = userRepository.getUserByUsername(user.getUsername());
    if(u != null){
        userExists = true;
        message = "Username Already Present!";
    }
    return Arrays.asList(userExists, message);
}
    public boolean comparePasswords(String password,String repeatPassword){
        return password.equals(repeatPassword);
    }

}
