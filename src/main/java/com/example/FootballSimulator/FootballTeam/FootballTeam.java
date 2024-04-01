package com.example.FootballSimulator.FootballTeam;

import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.League.League;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class FootballTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private BaseFootballTeam baseFootballTeam;

    @OneToMany(mappedBy = "footballTeam")
    private List<FootballPlayer> playerList;
    @ManyToOne
    @JoinColumn(name="league_id")
    private League league;

    private Integer budged;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<FootballPlayer> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<FootballPlayer> playerList) {
        this.playerList = playerList;
    }
}
