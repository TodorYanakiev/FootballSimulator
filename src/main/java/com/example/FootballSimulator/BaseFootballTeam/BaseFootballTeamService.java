package com.example.FootballSimulator.BaseFootballTeam;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class BaseFootballTeamService {
    private BaseFootballTeamRepository baseFootballTeamRepository;

    public BaseFootballTeamService(BaseFootballTeamRepository baseFootballTeamRepository) {
        this.baseFootballTeamRepository = baseFootballTeamRepository;
    }

    public String allTeams(Model model) {
        model.addAttribute("baseTeams", baseFootballTeamRepository.findAll());
        return "/base-football-team/all";
    }

    public String teamForm(Model model) {
        model.addAttribute("baseTeam", new BaseFootballTeam());
        return "/base-football-team/add";
    }

    public String teamSubmit(@Valid BaseFootballTeam baseTeam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/base-football-team/add";
        }
        baseFootballTeamRepository.save(baseTeam);
        return "redirect:/base-football-team/all";
    }
}
