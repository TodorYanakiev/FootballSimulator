package com.example.FootballSimulator.Standings;

import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.League.League;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
public class Standing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "football_team_id")
    private FootballTeam footballTeam;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;

    @Min(0)
    private Byte points;

    @Min(0)
    private Short scoredGoals;

    @Min(0)
    private Short concededGoals;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FootballTeam getFootballTeam() {
        return footballTeam;
    }

    public void setFootballTeam(FootballTeam footballTeam) {
        this.footballTeam = footballTeam;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Byte getPoints() {
        return points;
    }

    public void setPoints(Byte points) {
        this.points = points;
    }

    public Short getScoredGoals() {
        return scoredGoals;
    }

    public void setScoredGoals(Short scoredGoals) {
        this.scoredGoals = scoredGoals;
    }

    public Short getConcededGoals() {
        return concededGoals;
    }

    public void setConcededGoals(Short concededGoals) {
        this.concededGoals = concededGoals;
    }
}
