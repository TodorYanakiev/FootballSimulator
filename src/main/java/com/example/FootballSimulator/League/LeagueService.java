package com.example.FootballSimulator.League;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeamRepository;
import com.example.FootballSimulator.Constants.Role;
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
import com.example.FootballSimulator.Standings.Standing;
import com.example.FootballSimulator.Standings.StandingRepository;
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
import java.util.stream.Collectors;

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
    @Autowired
    private StandingRepository standingRepository;

    public String addLeague(Model model) {
        model.addAttribute("league", new League());
        model.addAttribute("allBaseFootballTeams", baseFootballTeamRepository.findAll());
        model.addAttribute("baseFootballTeams", new ArrayList<BaseFootballTeam>());
        return "/league/addLeague";
    }

    public String getLeague(Model model) {
        List<League> leagueList = (List<League>) leagueRepository.findAll();
        List<League> startedLeagues = leagueList.stream().filter(league -> league.getLeagueStatus().equals(Status.STARTED)).collect(Collectors.toList());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(authentication.getName());
        if (user.getRole().equals(Role.ROLE_USER)) {
            model.addAttribute("getAllLeagues", startedLeagues);
        } else {
            model.addAttribute("getAllLeagues", leagueList);
        }
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

    public String startLeague(Long leagueId, Model model) {
        Optional<League> optionalLeague = leagueRepository.findById(leagueId);
        if (optionalLeague.isPresent()) {
            League league = optionalLeague.get();
            if (getCheckMessageIfLeagueIsAbleToStart(league) == null) {
                league.setLeagueStatus(Status.STARTED);
                leagueRepository.save(league);
                addStandings(league);
            } else {
                model.addAttribute("message", getCheckMessageIfLeagueIsAbleToStart(league));
            }
        }
        model.addAttribute("getAllLeagues", leagueRepository.findAll());
        return "/league/getLeagues";
    }

    private void addStandings(League league) {
        List<FootballTeam> footballTeamList = league.getFootballTeamList();
        List<Standing> standings = new ArrayList<>();
        for (FootballTeam footballTeam : footballTeamList) {
            Standing standing = new Standing();
            standing.setFootballTeam(footballTeam);
            standing.setLeague(league);
            standing.setPoints((byte) 0);
            standing.setScoredGoals((short)0);
            standing.setConcededGoals((short)0);
            standingRepository.save(standing);
            footballTeam.setStanding(standing);
            footballTeamRepository.save(footballTeam);
            standings.add(standing);
        }
        league.setStandings(standings);
        leagueRepository.save(league);
    }

    private String getCheckMessageIfLeagueIsAbleToStart(League league) {
        if (league.getLeagueStatus() != Status.NOT_STARTED) {
            return "The league has already started or has ended!";
        }
        List<FootballTeam> teamList = league.getFootballTeamList();
        if (teamList.size() < 6) {
            return "The league must have at least 6 teams!";
        }
        for (FootballTeam footballTeam : teamList) {
            if (footballTeam.getPlayerList().size() < 16) {
                return "Every team must have at least 16 players!";
            }
            if (footballTeam.getLineUp() == null) {
                return "Every team must have line-up!";
            }
        }
        return null;
    }

    public String selectLeague(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(authentication.getName());
        if (user.getFootballTeam() != null) {
            model.addAttribute("footballTeam", user.getFootballTeam());
            return "redirect:/football-team/view/" + user.getFootballTeam().getId();
        }
        List<League> leagueList = leagueRepository.findAllByLeagueStatus(Status.STARTED);
        model.addAttribute("leagues", leagueList);
        return "/league/select";
    }
}
