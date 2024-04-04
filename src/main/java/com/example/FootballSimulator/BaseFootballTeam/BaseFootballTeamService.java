package com.example.FootballSimulator.BaseFootballTeam;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

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
        model.addAttribute("baseFootballTeam", new BaseFootballTeam());
        return "/base-football-team/add";
    }

    public String teamSubmit(@Valid @ModelAttribute("baseFootballTeam") BaseFootballTeam baseFootballTeam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/base-football-team/add";
        }
        baseFootballTeamRepository.save(baseFootballTeam);
        return "redirect:/base-football-team/all";
    }
}
