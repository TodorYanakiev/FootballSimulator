package com.example.FootballSimulator.LineUp;

import com.example.FootballSimulator.Constants.Position;
import com.example.FootballSimulator.Constants.TeamFormation;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import jakarta.persistence.*;

import java.util.Map;

@Entity
public class LineUp {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "football_team_id")
    private FootballTeam footballTeam;

    @Enumerated(EnumType.STRING)
    private TeamFormation footballFormation;

    @ElementCollection
    @CollectionTable(name = "lineup_positions", joinColumns = @JoinColumn(name = "lineup_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "football_player_id")
    private Map<Position, FootballPlayer> positionFootballPlayerMap;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
