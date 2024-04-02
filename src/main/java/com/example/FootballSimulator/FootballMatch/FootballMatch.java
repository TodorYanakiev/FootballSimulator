package com.example.FootballSimulator.FootballMatch;

import com.example.FootballSimulator.Constants.MatchStatus;
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

    @ManyToOne
    @JoinColumn(name = "game_week_id")
    private GameWeek gameWeek;


    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;

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

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }
}
