package com.example.FootballSimulator.LineUp;

import com.example.FootballSimulator.FootballTeam.FootballTeamRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/line-up")

public class LineUpController {
    private LineUpService lineUpService;

    private LineUpRepository lineUpRepository;

    private FootballTeamRepository footballTeamRepository;

    public LineUpController(LineUpService lineUpService, LineUpRepository lineUpRepository, FootballTeamRepository footballTeamRepository) {
        this.lineUpService = lineUpService;
        this.lineUpRepository = lineUpRepository;
        this.footballTeamRepository = footballTeamRepository;
    }

    @GetMapping("/add/{teamId}")
    public String addLineUpFormation(@PathVariable("teamId") Long teamId, Model model) {
        return lineUpService.addLineUpFormation(teamId, model);
    }

    @GetMapping("/add-players")
    public String addPlayersToLineUp(LineUp lineUp, Model model) {
        return lineUpService.addPlayersToLineUp(lineUp, model);
    }

    @PostMapping("/submit")
    public String submitLineUp(LineUp lineUp, Model model) {
        return lineUpService.submitLineUp(lineUp, model);
    }
}
