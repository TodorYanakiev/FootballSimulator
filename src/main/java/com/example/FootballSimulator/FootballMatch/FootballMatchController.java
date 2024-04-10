package com.example.FootballSimulator.FootballMatch;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/football-match")
public class FootballMatchController {
    private FootballMatchRepository footballMatchRepository;

    private FootballMatchService footballMatchService;

    public FootballMatchController(FootballMatchRepository footballMatchRepository, FootballMatchService footballMatchService) {
        this.footballMatchRepository = footballMatchRepository;
        this.footballMatchService = footballMatchService;
    }

    @GetMapping("/view/{matchId}")
    public String viewMatch(@PathVariable("matchId") Long matchId, Model model) {
        return footballMatchService.viewMatch(matchId, model);
    }
}
