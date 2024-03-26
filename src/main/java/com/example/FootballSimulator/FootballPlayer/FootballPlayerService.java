package com.example.FootballSimulator.FootballPlayer;

import com.example.FootballSimulator.Position;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class FootballPlayerService {
    private FootballPlayerRepository footballPlayerRepository;

    public FootballPlayerService(FootballPlayerRepository footballPlayerRepository) {
        this.footballPlayerRepository = footballPlayerRepository;
    }

    public String allPlayers(Model model) {
        model.addAttribute("players", footballPlayerRepository.findAll());
        return "/football-player/all";
    }

    public String playerForm(Model model) {
        model.addAttribute("footballPlayer", new FootballPlayer());
        model.addAttribute("positions", Position.values());
        return "/football-player/add";
    }

    public String playerSubmit(@Valid FootballPlayer player, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("positions", Position.values());
            return "/football-player/add";
        }
        footballPlayerRepository.save(player);
        return "redirect:/football-player/all";
    }
}
