package com.example.FootballSimulator.League;

import com.example.FootballSimulator.Constants.Status;
import com.example.FootballSimulator.FootballPlayer.FootballPlayerController;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.GameWeek.GameWeek;
import com.example.FootballSimulator.Standings.Standing;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Stack;

@Entity
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    @Size(min = 3)
    private String name;


    @OneToMany(mappedBy = "league")
    @Size(min = 6)
    private List<FootballTeam> footballTeamList;

    @OneToMany(mappedBy = "league")
    private List<GameWeek> gameWeekList;

    @Enumerated(EnumType.STRING)
    private Status leagueStatus;

    @OneToMany(mappedBy = "league")
    private List<Standing> standings;

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

    public List<FootballTeam> getFootballTeamList() {
        return footballTeamList;
    }

    public void setFootballTeamList(List<FootballTeam> footballTeamList) {
        this.footballTeamList = footballTeamList;
    }

    public List<GameWeek> getGameWeekList() {
        return gameWeekList;
    }

    public void setGameWeekList(List<GameWeek> gameWeekList) {
        this.gameWeekList = gameWeekList;
    }

    public Status getLeagueStatus() {
        return leagueStatus;
    }

    public void setLeagueStatus(Status leagueStatus) {
        this.leagueStatus = leagueStatus;
    }

    public List<Standing> getStandings() {
        return standings;
    }

    public void setStandings(List<Standing> standings) {
        this.standings = standings;
    }
}
