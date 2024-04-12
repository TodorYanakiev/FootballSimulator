package com.example.FootballSimulator.BaseFootballPlayer;

import com.example.FootballSimulator.Constants.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class BaseFootballPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    @Size(min = 2)
    private String firstName;

    @NotEmpty
    @Size(min = 2)
    private String lastName;

    @NotEmpty
    @Size(min = 2)
    private String nationality;

    @NotNull
    @Min(15)
    private Byte age;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte shirtNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Position position;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte startDefending;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte startSpeed;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte startDribble;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte startScoring;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte startPassing;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte startStamina;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte startPositioning;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte startGoalkeeping;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    public Byte getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(Byte shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Byte getStartDefending() {
        return startDefending;
    }

    public void setStartDefending(Byte startDefending) {
        this.startDefending = startDefending;
    }

    public Byte getStartSpeed() {
        return startSpeed;
    }

    public void setStartSpeed(Byte startSpeed) {
        this.startSpeed = startSpeed;
    }

    public Byte getStartDribble() {
        return startDribble;
    }

    public void setStartDribble(Byte startDribble) {
        this.startDribble = startDribble;
    }

    public Byte getStartScoring() {
        return startScoring;
    }

    public void setStartScoring(Byte startScoring) {
        this.startScoring = startScoring;
    }

    public Byte getStartPassing() {
        return startPassing;
    }

    public void setStartPassing(Byte startPassing) {
        this.startPassing = startPassing;
    }

    public Byte getStartStamina() {
        return startStamina;
    }

    public void setStartStamina(Byte startStamina) {
        this.startStamina = startStamina;
    }

    public Byte getStartPositioning() {
        return startPositioning;
    }

    public void setStartPositioning(Byte startPositioning) {
        this.startPositioning = startPositioning;
    }

    public Byte getStartGoalkeeping() {
        return startGoalkeeping;
    }

    public void setStartGoalkeeping(Byte startGoalkeeping) {
        this.startGoalkeeping = startGoalkeeping;
    }


}
