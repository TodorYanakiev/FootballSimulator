package com.example.FootballSimulator.FootballPlayer;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.League.League;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FootballPlayerRepository extends CrudRepository<FootballPlayer, Long> {
    @Query("SELECT fp FROM FootballPlayer fp WHERE fp.footballTeam.league.id = :leagueId")
    List<FootballPlayer> findByFootballTeam_LeagueId(Long leagueId);
    List<FootballPlayer> findAllByIdIn(List<Long> ids);
    //List<FootballPlayer> findByTeamId(Long teamId);
    @Query("SELECT p FROM FootballPlayer p WHERE p.footballTeam.id = :teamId")
    List<FootballPlayer> getPlayersByTeamId(Long teamId);
    @Query("SELECT p FROM FootballPlayer p WHERE p.footballPlayerStatus = true AND p.footballTeam.id <> ?1")
    List<FootballPlayer> findForSalePlayersExceptTeamId(Long teamId);
    @Query("SELECT fp FROM FootballPlayer fp WHERE fp.footballTeam = :team")
    List<FootballPlayer> findByFootballTeam(@Param("team") FootballTeam footballTeam);
    @Query("SELECT p FROM FootballPlayer p WHERE p.footballPlayerStatus = true AND p.footballTeam.league = (SELECT t.league FROM FootballTeam t WHERE t.id = ?1) AND p.footballTeam.id <> ?1")
    List<FootballPlayer> findForSalePlayersInLeagueExceptTeamId(Long teamId);
}
