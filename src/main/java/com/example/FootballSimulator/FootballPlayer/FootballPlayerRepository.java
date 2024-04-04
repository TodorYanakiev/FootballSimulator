package com.example.FootballSimulator.FootballPlayer;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.League.League;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FootballPlayerRepository extends CrudRepository<FootballPlayer, Long> {
    @Query("SELECT fp FROM FootballPlayer fp WHERE fp.footballTeam.league = :league")
    List<FootballPlayer> findByFootballTeam_League(League league);
    List<FootballPlayer> findAllByIdIn(List<Long> ids);
    //List<FootballPlayer> findByTeamId(Long teamId);
}
