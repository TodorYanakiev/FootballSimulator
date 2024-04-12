package com.example.FootballSimulator.Config;

import com.example.FootballSimulator.User.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

 
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/league/select", "/football-team/select-team/**","/football-team/saleFootballPlayers/**","/football-team/getFootballPlayersForSale"
                                , "/football-team/buy-players-for-home-team/**","/football-team/show-bought-players").hasAuthority("ROLE_USER")
                        .requestMatchers("/base**", "/league/add", "/league/submit", "/league/start/**",
                                "/football-team/add/**", "/football-team/submit").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/league/get", "/football-team/all/**", "/football-team/players/**",
                                "/game-week/all/**", "/football-team/players/**", "/football-team/getFootballPlayers**",
                                "/football-team/view/**").authenticated()
                        .anyRequest().authenticated()
                )//transfer requests?
                .formLogin((form) -> form
                        .loginPage("/auth/login")
                        .usernameParameter("usernameOrEmail")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());
        return http.build();
    }
}