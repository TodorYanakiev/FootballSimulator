package com.example.FootballSimulator.BaseFootballPlayer;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/base-football-player")
public class BaseFootballPlayerController {
    private BaseFootballPlayerRepository baseFootballPlayerRepository;

    private BaseFootballPlayerService baseFootballPlayerService;

    public BaseFootballPlayerController(BaseFootballPlayerRepository baseFootballPlayerRepository, BaseFootballPlayerService baseFootballPlayerService) {
        this.baseFootballPlayerRepository = baseFootballPlayerRepository;
        this.baseFootballPlayerService = baseFootballPlayerService;
    }

    @GetMapping("/all")
    public String allPlayers(Model model){
        return baseFootballPlayerService.allPlayers(model);
    }

    @GetMapping("/add")
    public String playerForm(Model model) {
        return baseFootballPlayerService.playerForm(model);
    }

    @PostMapping("/add")
    public String playerSubmit(@Valid BaseFootballPlayer player, BindingResult bindingResult, Model model) {
        return baseFootballPlayerService.playerSubmit(player, bindingResult, model);
    }
}
