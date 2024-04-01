package com.example.FootballSimulator.League;

import com.example.FootballSimulator.FootballPlayer.FootballPlayerController;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "league")
    private List<FootballTeam> footballTeamList;

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
}
