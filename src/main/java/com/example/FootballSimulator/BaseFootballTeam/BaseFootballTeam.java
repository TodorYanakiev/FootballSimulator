package com.example.FootballSimulator.BaseFootballTeam;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class BaseFootballTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    @Size(min = 3)
    private String name;

    @NotEmpty
    @Size(min = 2, max = 4)
    private String abbreviation; //if Barcelona then BAR

    @NotEmpty
    @Size(min = 3)
    private String stadiumName;

    @NotNull
    @Min(0)
    private Integer startBudged;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public Integer getStartBudged() {
        return startBudged;
    }

    public void setStartBudged(Integer startBudged) {
        this.startBudged = startBudged;
    }
}
