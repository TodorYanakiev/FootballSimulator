package com.example.FootballSimulator.FootballPlayer;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
public class FootballPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "base_player_id")
    private BaseFootballPlayer baseFootballPlayer;

    @ManyToOne
    @JoinColumn(name = "football_team_id")
    private FootballTeam footballTeam;

    @NotNull
    @Min(0)
    private Integer price;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte defending;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte speed;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte dribble;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte scoring;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte passing;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte stamina;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte positioning;

    @NotNull
    @Min(1)
    @Max(99)
    private Byte goalkeeping;
    @NotNull
    private boolean footballPlayerStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BaseFootballPlayer getBaseFootballPlayer() {
        return baseFootballPlayer;
    }

    public void setBaseFootballPlayer(BaseFootballPlayer baseFootballPlayer) {
        this.baseFootballPlayer = baseFootballPlayer;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public FootballTeam getFootballTeam() {
        return footballTeam;
    }

    public void setFootballTeam(FootballTeam footballTeam) {
        this.footballTeam = footballTeam;
    }

    public boolean isFootballPlayerStatus() {
        return footballPlayerStatus;
    }

    public void setFootballPlayerStatus(boolean footballPlayerStatus) {
        this.footballPlayerStatus = footballPlayerStatus;
    }
}
