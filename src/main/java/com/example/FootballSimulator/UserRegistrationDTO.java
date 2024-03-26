package com.example.FootballSimulator;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO {
    @NotEmpty(message = "The field must not be empty!")
    @Size(max = 50,message = "The maximum length is 50 characters!")
    private String name;
    @NotEmpty(message = "The field must not be empty!")
    @Size(max = 50,message = "The maximum length is 50 characters!")
    private String lastName;
    @NotEmpty(message = "The field must not be empty!")
    @Size(min = 4, message = "The minimum length is 4 characters!")
    @Size(max = 20, message = "The maximum length is 20 characters!")
    private String username;
    @NotEmpty(message = "The field must not be empty!")
    private String email;
    @NotEmpty(message = "The field must not be empty!")
    private String password;
    @NotEmpty(message = "The field must not be empty!")
    private String repeatPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
