package com.example.FootballSimulator.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        User userByUsername = userRepository.getUserByUsername(usernameOrEmail);
        User userByEmail = userRepository.getUserByEmail(usernameOrEmail);
        if(userByEmail != null ){
            return new MyUserDetails(userByEmail);
        } else if (userByUsername != null) {
            return new MyUserDetails(userByUsername);
        }
        else {
            throw new UsernameNotFoundException("Could not find user");
        }
    }

}