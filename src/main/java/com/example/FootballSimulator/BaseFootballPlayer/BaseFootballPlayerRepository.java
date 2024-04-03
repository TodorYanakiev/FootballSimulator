package com.example.FootballSimulator.BaseFootballPlayer;

import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BaseFootballPlayerRepository extends CrudRepository<BaseFootballPlayer, Long> {
    List<BaseFootballPlayer> findAllByIdIn(List<Long> ids);
}
