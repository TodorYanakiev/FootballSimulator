package com.example.FootballSimulator.FootballMatch;

import com.example.FootballSimulator.Constants.Status;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.GameWeek.GameWeek;
import jakarta.persistence.*;

@Entity
public class FootballMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private FootballTeam homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private FootballTeam awayTeam;

    private Byte homeTeamScore;

    private Byte awayTeamScore;

    private Byte homeAttacks;

    private Byte awayAttacks;

    private Byte dangerHomeAttacks;

    private Byte dangerAwayAttacks;

    private Byte homeShots;

    private Byte awayShots;

    @ManyToOne
    @JoinColumn(name = "game_week_id")
    private GameWeek gameWeek;


    @Enumerated(EnumType.STRING)
    private Status matchStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FootballTeam getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(FootballTeam homeTeam) {
        this.homeTeam = homeTeam;
    }

    public FootballTeam getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(FootballTeam awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Byte getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(Byte homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public Byte getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(Byte awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public GameWeek getGameWeek() {
        return gameWeek;
    }

    public void setGameWeek(GameWeek gameWeek) {
        this.gameWeek = gameWeek;
    }

    public Status getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(Status matchStatus) {
        this.matchStatus = matchStatus;
    }

    public Byte getHomeAttacks() {
        return homeAttacks;
    }

    public void setHomeAttacks(Byte homeAttacks) {
        this.homeAttacks = homeAttacks;
    }

    public Byte getAwayAttacks() {
        return awayAttacks;
    }

    public void setAwayAttacks(Byte awayAttacks) {
        this.awayAttacks = awayAttacks;
    }

    public Byte getHomeShots() {
        return homeShots;
    }

    public void setHomeShots(Byte homeShots) {
        this.homeShots = homeShots;
    }

    public Byte getAwayShots() {
        return awayShots;
    }

    public void setAwayShots(Byte awayShots) {
        this.awayShots = awayShots;
    }

    public Byte getDangerHomeAttacks() {
        return dangerHomeAttacks;
    }

    public void setDangerHomeAttacks(Byte dangerHomeAttacks) {
        this.dangerHomeAttacks = dangerHomeAttacks;
    }

    public Byte getDangerAwayAttacks() {
        return dangerAwayAttacks;
    }

    public void setDangerAwayAttacks(Byte dangerAwayAttacks) {
        this.dangerAwayAttacks = dangerAwayAttacks;
    }
}
