package com.example.FootballSimulator.BaseFootballTeam;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface BaseFootballTeamRepository extends CrudRepository<BaseFootballTeam, Long> {

    List<BaseFootballTeam> findAllByIdIn(List<Long> ids);
}
