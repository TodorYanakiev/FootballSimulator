package com.example.FootballSimulator.GameWeek;

import com.example.FootballSimulator.FootballMatch.FootballMatch;
import com.example.FootballSimulator.League.League;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class GameWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private Byte weekNumber;

    @OneToMany(mappedBy = "gameWeek")
    private List<FootballMatch> matchList;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Byte weekNumber) {
        this.weekNumber = weekNumber;
    }

    public List<FootballMatch> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<FootballMatch> matchList) {
        this.matchList = matchList;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }
}
