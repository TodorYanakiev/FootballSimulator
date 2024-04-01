package com.example.FootballSimulator.BaseFootballPlayer;

import com.example.FootballSimulator.Constants.Position;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class BaseFootballPlayerService {
    private BaseFootballPlayerRepository baseFootballPlayerRepository;

    public BaseFootballPlayerService(BaseFootballPlayerRepository footballPlayerRepository) {
        this.baseFootballPlayerRepository = footballPlayerRepository;
    }

    public String allPlayers(Model model) {
        model.addAttribute("basePlayers", baseFootballPlayerRepository.findAll());
        return "/base-football-player/all";
    }

    public String playerForm(Model model) {
        model.addAttribute("baseFootballPlayer", new BaseFootballPlayer());
        model.addAttribute("positions", Position.values());
        return "/base-football-player/add";
    }

    public String playerSubmit(@Valid BaseFootballPlayer player, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("positions", Position.values());
            return "/base-football-player/add";
        }
        baseFootballPlayerRepository.save(player);
        return "redirect:/base-football-player/all";
    }
}
