package com.example.FootballSimulator.League;

import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeamRepository;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.FootballTeam.FootballTeamRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeagueService {
    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private BaseFootballTeamRepository baseFootballTeamRepository;
    @Autowired
    private FootballTeamRepository footballTeamRepository;
    public String addLeague(Model model){
        model.addAttribute("league",new League());
        model.addAttribute("allBaseFootballTeams",baseFootballTeamRepository.findAll());
        model.addAttribute("baseFootballTeams",new ArrayList<BaseFootballTeam>());
        return "/league/addLeague";
    }
    public String getLeague(Model model){
        model.addAttribute("getAllLeagues",leagueRepository.findAll());
        return "/league/getLeagues";
    }
    public String submitLeague(@Valid League league, BindingResult bindingResult, Model model,@RequestParam("selectedFootballTeamIds") List<Long> selectedFootballTeamIds){
        if (bindingResult.hasErrors()){
            model.addAttribute("allBaseFootballTeams",baseFootballTeamRepository.findAll());
            return "/league/addLeague";

        }
        List<BaseFootballTeam> selectedFootballTeams = baseFootballTeamRepository.findAllByIdIn(selectedFootballTeamIds);
        leagueRepository.save(league);
        for (int i = 0; i < selectedFootballTeams.size(); i++) {
            FootballTeam footballTeam = new FootballTeam();
            footballTeam.setBaseFootballTeam(selectedFootballTeams.get(i));
            footballTeam.setBudged(selectedFootballTeams.get(i).getStartBudged());
            footballTeam.setLeague(league);
            footballTeamRepository.save(footballTeam);
        }
        return "redirect:/league/get";
    }
}
