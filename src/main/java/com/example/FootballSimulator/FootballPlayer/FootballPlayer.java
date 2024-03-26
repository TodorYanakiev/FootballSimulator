package com.example.FootballSimulator.FootballPlayer;

import com.example.FootballSimulator.FootballTeam;
import com.example.FootballSimulator.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class FootballPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private FootballTeam team;

    @NotEmpty
    @Size(min = 2)
    private String name;

    @NotEmpty
    @Size(min = 2)
    private String lastName;

    @NotEmpty
    @Size(min = 2)
    private String nationality;

    @NotEmpty
    @Min(1)
    @Max(99)
    private Byte shirtNumber;

    @NotNull
    private Position position;

    @NotEmpty
    @Min(1)
    @Max(99)
    private Byte defending;

    @NotEmpty
    @Min(1)
    @Max(99)
    private Byte speed;

    @NotEmpty
    @Min(1)
    @Max(99)
    private Byte dribble;

    @NotEmpty
    @Min(1)
    @Max(99)
    private Byte scoring;

    @NotEmpty
    @Min(1)
    @Max(99)
    private Byte passing;

    @NotEmpty
    @Min(1)
    @Max(99)
    private Byte stamina;

    @NotEmpty
    @Min(1)
    @Max(99)
    private Byte positioning;

    @NotEmpty
    @Min(1)
    @Max(99)
    private Byte goalkeeping;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FootballTeam getTeam() {
        return team;
    }

    public void setTeam(FootballTeam team) {
        this.team = team;
    }

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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
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

    public Byte getDefending() {
        return defending;
    }

    public void setDefending(Byte defending) {
        this.defending = defending;
    }

    public Byte getSpeed() {
        return speed;
    }

    public void setSpeed(Byte speed) {
        this.speed = speed;
    }

    public Byte getDribble() {
        return dribble;
    }

    public void setDribble(Byte dribble) {
        this.dribble = dribble;
    }

    public Byte getScoring() {
        return scoring;
    }

    public void setScoring(Byte scoring) {
        this.scoring = scoring;
    }

    public Byte getPassing() {
        return passing;
    }

    public void setPassing(Byte passing) {
        this.passing = passing;
    }

    public Byte getStamina() {
        return stamina;
    }

    public void setStamina(Byte stamina) {
        this.stamina = stamina;
    }

    public Byte getPositioning() {
        return positioning;
    }

    public void setPositioning(Byte positioning) {
        this.positioning = positioning;
    }

    public Byte getGoalkeeping() {
        return goalkeeping;
    }

    public void setGoalkeeping(Byte goalkeeping) {
        this.goalkeeping = goalkeeping;
    }
}
