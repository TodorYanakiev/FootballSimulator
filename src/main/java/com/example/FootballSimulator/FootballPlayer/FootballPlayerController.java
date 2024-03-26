package com.example.FootballSimulator.FootballPlayer;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/football-player")
public class FootballPlayerController {
    private FootballPlayerRepository footballPlayerRepository;

    private FootballPlayerService footballPlayerService;

    public FootballPlayerController(FootballPlayerRepository footballPlayerRepository, FootballPlayerService footballPlayerService) {
        this.footballPlayerRepository = footballPlayerRepository;
        this.footballPlayerService = footballPlayerService;
    }

    @GetMapping("/all")
    public String allPlayers(Model model){
        return footballPlayerService.allPlayers(model);
    }

    @GetMapping("/add")
    public String playerForm(Model model) {
        return footballPlayerService.playerForm(model);
    }

    @PostMapping("/add")
    public String playerSubmit(@Valid FootballPlayer player, BindingResult bindingResult, Model model) {
        return footballPlayerService.playerSubmit(player, bindingResult, model);
    }
}
