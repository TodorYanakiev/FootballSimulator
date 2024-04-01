package com.example.FootballSimulator.Football_Match;

import com.example.FootballSimulator.Constants.MatchStatus;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.GameWeek.GameWeek;
import jakarta.persistence.*;

@Entity
public class Football_Match {
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
}
