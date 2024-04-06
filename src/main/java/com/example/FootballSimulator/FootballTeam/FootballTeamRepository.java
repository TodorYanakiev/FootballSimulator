package com.example.FootballSimulator.FootballTeam;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FootballTeamRepository extends CrudRepository<FootballTeam,Long> {
    @Query("SELECT t FROM FootballTeam t WHERE t.id <> :teamId")
    List<FootballTeam> findAllExceptTeamId(@Param("teamId") Long teamId);
}
