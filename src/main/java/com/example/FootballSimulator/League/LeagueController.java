package com.example.FootballSimulator.League;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/league")
public class LeagueController {
    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private LeagueService leagueService;
    @GetMapping("/add")
    public String addLeague(Model model){
        return leagueService.addLeague(model);
    }
    @GetMapping("/get")
    public String getLeague(Model model){
       return leagueService.getLeague(model);
    }
    @PostMapping("/submit")
    public String submitLeague(@Valid League league, BindingResult bindingResult,Model model){
        return leagueService.submitLeague(league,bindingResult,model);
    }
}
