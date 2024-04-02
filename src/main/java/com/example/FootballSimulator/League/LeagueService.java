package com.example.FootballSimulator.League;

import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeamRepository;
import com.example.FootballSimulator.FootballMatch.FootballMatch;
import com.example.FootballSimulator.FootballMatch.FootballMatchRepository;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.FootballTeam.FootballTeamRepository;
import com.example.FootballSimulator.GameWeek.GameWeek;
import com.example.FootballSimulator.GameWeek.GameWeekManager;
import com.example.FootballSimulator.GameWeek.GameWeekRepository;
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

    @Autowired
    private GameWeekRepository gameWeekRepository;

    @Autowired
    private FootballMatchRepository footballMatchRepository;

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
        List<FootballTeam> footballTeamList = new ArrayList<>();
        for (int i = 0; i < selectedFootballTeams.size(); i++) {
            FootballTeam footballTeam = new FootballTeam();
            footballTeam.setBaseFootballTeam(selectedFootballTeams.get(i));
            footballTeam.setBudged(selectedFootballTeams.get(i).getStartBudged());
            footballTeam.setLeague(league);
            footballTeamList.add(footballTeam);
            footballTeamRepository.save(footballTeam);
        }
        league.setFootballTeamList(footballTeamList);
        GameWeekManager gameWeekManager = new GameWeekManager();
        List<GameWeek> gameWeekList = gameWeekManager.generateGameWeeks(league);
        int size = gameWeekList.size();
        for (int i = 0; i < size; i++) {
            gameWeekRepository.save(gameWeekList.get(i));
            List<FootballMatch> matchList = gameWeekList.get(i).getMatchList();
            int numberOfMatches = matchList.size();
            for (int j = 0; j < numberOfMatches; j++) {
                footballMatchRepository.save(matchList.get(j));
            }
        }
        return "redirect:/league/get";
    }
}
