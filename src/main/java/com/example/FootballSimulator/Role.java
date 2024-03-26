package com.example.FootballSimulator;

public enum Role {
    ROLE_ADMIN("Admin"),
    ROLE_USER("USER");
    String label;
    Role (String label){
        this.label = label;
    }
    public String getLabel() {
        return label;
    }

}
