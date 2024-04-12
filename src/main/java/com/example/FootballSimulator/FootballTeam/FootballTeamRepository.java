package com.example.FootballSimulator.FootballTeam;

import com.example.FootballSimulator.League.League;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FootballTeamRepository extends CrudRepository<FootballTeam,Long> {
//    @Query("SELECT t FROM FootballTeam t WHERE t.id <> :teamId")
//    List<FootballTeam> findAllExceptTeamId(@Param("teamId") Long teamId);
    @Query("SELECT ft FROM FootballTeam ft WHERE ft.league = :league AND ft.id <> :teamId")
    List<FootballTeam> findAllInSameLeagueExceptTeam(@Param("league") League league, @Param("teamId") Long teamId);
}
