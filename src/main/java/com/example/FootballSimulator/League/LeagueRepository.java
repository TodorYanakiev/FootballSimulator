package com.example.FootballSimulator.League;

import com.example.FootballSimulator.Constants.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LeagueRepository extends CrudRepository<League,Long> {
    List<League> findAllByLeagueStatus(Status status);
}
