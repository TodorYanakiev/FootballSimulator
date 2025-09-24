package com.example.FootballSimulator.Season;

import com.example.FootballSimulator.FootballTeam.FootballTeam;
import jakarta.persistence.*;

@Entity
public class SeasonStanding {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne
    @JoinColumn(name = "football_team_id")
    private FootballTeam footballTeam;

    private byte points;

    private byte playedMatches;

    private short scoredGoals;

    private short concededGoals;

    private int position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public FootballTeam getFootballTeam() {
        return footballTeam;
    }

    public void setFootballTeam(FootballTeam footballTeam) {
        this.footballTeam = footballTeam;
    }

    public byte getPoints() {
        return points;
    }

    public void setPoints(byte points) {
        this.points = points;
    }

    public byte getPlayedMatches() {
        return playedMatches;
    }

    public void setPlayedMatches(byte playedMatches) {
        this.playedMatches = playedMatches;
    }

    public short getScoredGoals() {
        return scoredGoals;
    }

    public void setScoredGoals(short scoredGoals) {
        this.scoredGoals = scoredGoals;
    }

    public short getConcededGoals() {
        return concededGoals;
    }

    public void setConcededGoals(short concededGoals) {
        this.concededGoals = concededGoals;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
