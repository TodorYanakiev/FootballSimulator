package com.example.FootballSimulator;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/registration")
    public String register(Model model){
        model.addAttribute("userRegistrationDTO",new UserRegistrationDTO());
        return "registration";
    }
    @PostMapping("/submitRegistration")
    public String submitRegistration(@Valid UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult,Model model){
       return userService.addUser(userRegistrationDTO,bindingResult,model);
    }
}
