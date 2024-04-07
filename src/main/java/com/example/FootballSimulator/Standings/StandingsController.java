package com.example.FootballSimulator.Standings;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/standings")
public class StandingsController {
    private StandingsService standingsService;

    public StandingsController(StandingsService standingsService) {
        this.standingsService = standingsService;
    }

    @GetMapping("/view/{leagueId}")
    public String viewStandingsForLeague(@PathVariable("leagueId") Long leagueId, Model model) {
        return standingsService.viewStandingsForLeague(leagueId, model);
    }
}
