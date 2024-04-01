package com.example.FootballSimulator.GameWeek;

import com.example.FootballSimulator.Football_Match.Football_Match;
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
    private List<Football_Match> matchList;

//    private League league;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
