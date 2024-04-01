package com.example.FootballSimulator.League;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class LeagueService {
    @Autowired
    private LeagueRepository leagueRepository;
    public String addLeague(Model model){
        model.addAttribute("league",new League());
        return "/league/addLeague";
    }
    public String getLeague(Model model){
        model.addAttribute("getAllLeagues",leagueRepository.findAll());
        return "/league/getLeagues";
    }
    public String submitLeague(@Valid League league, BindingResult bindingResult,Model model){
        if (bindingResult.hasErrors()){
            return "/league/addLeague";
        }
        leagueRepository.save(league);
        return "redirect:/league/get";
    }
}
