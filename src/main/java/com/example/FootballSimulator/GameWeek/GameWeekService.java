package com.example.FootballSimulator.GameWeek;

import com.example.FootballSimulator.FootballMatch.FootballMatchRepository;
import org.springframework.stereotype.Service;

@Service
public class GameWeekService {
    private GameWeekRepository gameWeekRepository;

    private FootballMatchRepository footballMatchRepository;

    public GameWeekService(GameWeekRepository gameWeekRepository, FootballMatchRepository footballMatchRepository) {
        this.gameWeekRepository = gameWeekRepository;
        this.footballMatchRepository = footballMatchRepository;
    }

}
