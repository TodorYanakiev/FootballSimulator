package com.example.FootballSimulator.League;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeamRepository;
import com.example.FootballSimulator.Constants.Status;
import com.example.FootballSimulator.FootballMatch.FootballMatch;
import com.example.FootballSimulator.FootballMatch.FootballMatchRepository;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballPlayer.FootballPlayerRepository;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.FootballTeam.FootballTeamRepository;
import com.example.FootballSimulator.GameWeek.GameWeek;
import com.example.FootballSimulator.GameWeek.GameWeekManager;
import com.example.FootballSimulator.GameWeek.GameWeekRepository;
import com.example.FootballSimulator.User.User;
import com.example.FootballSimulator.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LeagueService {
    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private BaseFootballTeamRepository baseFootballTeamRepository;
    @Autowired
    private FootballTeamRepository footballTeamRepository;
    @Autowired
    private FootballPlayerRepository footballPlayerRepository;

    @Autowired
    private GameWeekRepository gameWeekRepository;

    @Autowired
    private FootballMatchRepository footballMatchRepository;
    @Autowired
    private BaseFootballPlayerRepository baseFootballPlayerRepository;
    @Autowired
    private UserRepository userRepository;

    public String addLeague(Model model) {
        model.addAttribute("league", new League());
        model.addAttribute("allBaseFootballTeams", baseFootballTeamRepository.findAll());
        model.addAttribute("baseFootballTeams", new ArrayList<BaseFootballTeam>());
        return "/league/addLeague";
    }

    public String getLeague(Model model) {
        model.addAttribute("getAllLeagues", leagueRepository.findAll());
        return "/league/getLeagues";
    }

    public String submitLeague(@Valid League league, BindingResult bindingResult, Model model, @RequestParam("selectedFootballTeamIds") List<Long> selectedFootballTeamIds) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allBaseFootballTeams", baseFootballTeamRepository.findAll());
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
        league.setLeagueStatus(Status.NOT_STARTED);
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

    public String startLeague(Long leagueId) {
        Optional<League> optionalLeague = leagueRepository.findById(leagueId);
        if (optionalLeague.isPresent()) {
            League league = optionalLeague.get();
            if (checkIfLeagueIsAbleToStart(league)) {
                league.setLeagueStatus(Status.STARTED);
                leagueRepository.save(league);
            }
        }
        return "redirect:/league/get";
    }

    private boolean checkIfLeagueIsAbleToStart(League league) {
        if (league.getLeagueStatus() != Status.NOT_STARTED) {
            return false;
        }
        List<FootballTeam> teamList = league.getFootballTeamList();
        if (teamList.size() < 6) {
            return false;
        }
        for (FootballTeam footballTeam : teamList) {
            if (footballTeam.getPlayerList().size() < 16) {
                return false;
            }
            if (footballTeam.getLineUp() == null) {
                return false;
            }
        }
        return true;
    }

    public String selectLeague(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(authentication.getName());
        if (user.getFootballTeam() != null) {
            model.addAttribute("footballTeam", user.getFootballTeam());
            return "/football-team/view";
        }
        List<League> leagueList = leagueRepository.findAllByLeagueStatus(Status.STARTED);
        model.addAttribute("leagues", leagueList);
        return "/league/select";
    }
}
