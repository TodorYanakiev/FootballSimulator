package com.example.FootballSimulator.FootballTeam;

import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.League.League;
import com.example.FootballSimulator.LineUp.LineUp;
import com.example.FootballSimulator.Standings.Standing;
import com.example.FootballSimulator.User.User;
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

    @OneToMany(mappedBy = "footballTeam", fetch = FetchType.EAGER)
    private List<FootballPlayer> playerList;
    @ManyToOne
    @JoinColumn(name="league_id")
    private League league;

    @OneToOne(mappedBy = "footballTeam")
    private LineUp lineUp;

    private Integer budged;

    @OneToOne(mappedBy = "footballTeam")
    private User user;

    @OneToOne(mappedBy = "footballTeam")
    private Standing standing;

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

    public BaseFootballTeam getBaseFootballTeam() {
        return baseFootballTeam;
    }

    public void setBaseFootballTeam(BaseFootballTeam baseFootballTeam) {
        this.baseFootballTeam = baseFootballTeam;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Integer getBudged() {
        return budged;
    }

    public void setBudged(Integer budged) {
        this.budged = budged;
    }

    public LineUp getLineUp() {
        return lineUp;
    }

    public void setLineUp(LineUp lineUp) {
        this.lineUp = lineUp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Standing getStanding() {
        return standing;
    }

    public void setStanding(Standing standing) {
        this.standing = standing;
    }
}
