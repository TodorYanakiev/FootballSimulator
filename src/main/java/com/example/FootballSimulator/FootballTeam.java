package com.example.FootballSimulator;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class FootballTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String logoPictureName;
    //private User manager;

    private String establishmentYear;

    @OneToMany
    private List<BaseFootballPlayer> playerList;

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

    public String getLogoPictureName() {
        return logoPictureName;
    }

    public void setLogoPictureName(String logoPictureName) {
        this.logoPictureName = logoPictureName;
    }

//    public User getManager() {
//        return manager;
//    }
//
//    public void setManager(User manager) {
//        this.manager = manager;
//    }

    public String getEstablishmentYear() {
        return establishmentYear;
    }

    public void setEstablishmentYear(String establishmentYear) {
        this.establishmentYear = establishmentYear;
    }

    public List<BaseFootballPlayer> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<BaseFootballPlayer> playerList) {
        this.playerList = playerList;
    }
}
