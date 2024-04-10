package com.example.FootballSimulator.GameWeek;

import com.example.FootballSimulator.League.League;
import com.example.FootballSimulator.League.LeagueRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/game-week")
public class GameWeekController {
    private GameWeekRepository gameWeekRepository;

    private LeagueRepository leagueRepository;

    private GameWeekService gameWeekService;

    public GameWeekController(GameWeekRepository gameWeekRepository, LeagueRepository leagueRepository, GameWeekService gameWeekService) {
        this.gameWeekRepository = gameWeekRepository;
        this.leagueRepository = leagueRepository;
        this.gameWeekService = gameWeekService;
    }

    @GetMapping("/all/{leagueId}")
    public String showGameWeekAndMatchesForLeague(@PathVariable("leagueId") Long leagueId, Model model) {
        Optional<League> optionalLeague = leagueRepository.findById(leagueId);
        if (optionalLeague.isPresent()) {
            model.addAttribute("league", optionalLeague.get());
            return "/game-week/all-for-league";
        } else {
            return "redirect:/league/get";
        }
    }

    @GetMapping("/simulate-week")
    public String simulateGameWeek(Model model) {
        return gameWeekService.simulateGameWeek(model);
    }
}
