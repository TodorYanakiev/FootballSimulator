package com.example.FootballSimulator.BaseFootballTeam;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/base-football-team")
public class BaseFootballTeamController {
    private BaseFootballTeamRepository baseFootballTeamRepository;

    private BaseFootballTeamService baseFootballTeamService;

    public BaseFootballTeamController(BaseFootballTeamRepository baseFootballTeamRepository, BaseFootballTeamService baseFootballTeamService) {
        this.baseFootballTeamRepository = baseFootballTeamRepository;
        this.baseFootballTeamService = baseFootballTeamService;
    }

    @GetMapping("/all")
    public String allTeams(Model model) {
        return baseFootballTeamService.allTeams(model);
    }

    @GetMapping("/add")
    public String addTeam(Model model) {
        return baseFootballTeamService.teamForm(model);
    }

    @PostMapping("/add")
    public String submitTeam(@Valid BaseFootballTeam baseFootballTeam, BindingResult bindingResult) {
        return baseFootballTeamService.teamSubmit(baseFootballTeam, bindingResult);
    }
}
