package com.example.FootballSimulator.League;

import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeamRepository;
import com.example.FootballSimulator.Constants.Role;
import com.example.FootballSimulator.Constants.Status;
import com.example.FootballSimulator.FootballMatch.FootballMatch;
import com.example.FootballSimulator.FootballMatch.FootballMatchRepository;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.FootballTeam.FootballTeamRepository;
import com.example.FootballSimulator.GameWeek.GameWeek;
import com.example.FootballSimulator.GameWeek.GameWeekManager;
import com.example.FootballSimulator.GameWeek.GameWeekRepository;
import com.example.FootballSimulator.Season.Season;
import com.example.FootballSimulator.Season.SeasonRepository;
import com.example.FootballSimulator.Season.SeasonStanding;
import com.example.FootballSimulator.Standings.Standing;
import com.example.FootballSimulator.Standings.StandingRepository;
import com.example.FootballSimulator.User.User;
import com.example.FootballSimulator.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
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
    private GameWeekRepository gameWeekRepository;

    @Autowired
    private FootballMatchRepository footballMatchRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StandingRepository standingRepository;
    @Autowired
    private SeasonRepository seasonRepository;

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
        if (selectedFootballTeamIds == null) {
            model.addAttribute("message","There must be at least 6 teams!");
            model.addAttribute("allBaseFootballTeams", baseFootballTeamRepository.findAll());
            return "/league/addLeague";
        }
        List<BaseFootballTeam> selectedFootballTeams = baseFootballTeamRepository.findAllByIdIn(selectedFootballTeamIds);
        if (bindingResult.hasErrors() || selectedFootballTeams.size() % 2 == 1) {
            model.addAttribute("message","There must be an even number of teams!");
            model.addAttribute("allBaseFootballTeams", baseFootballTeamRepository.findAll());
            return "/league/addLeague";
        }

        league.setCurrentSeason(1);
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
                if (league.getCurrentSeason() == null) {
                    league.setCurrentSeason(1);
                }
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
            standing.setPlayedMatches((byte)0);
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

    @Transactional
    public String startNewSeason(Long leagueId, Model model) {
        Optional<League> optionalLeague = leagueRepository.findById(leagueId);
        if (optionalLeague.isEmpty()) {
            model.addAttribute("message", "No such league!");
            model.addAttribute("getAllLeagues", leagueRepository.findAll());
            return "/league/getLeagues";
        }
        League league = optionalLeague.get();
        if (!Status.FINISHED.equals(league.getLeagueStatus())) {
            model.addAttribute("message", "The league must be finished before starting a new season!");
            model.addAttribute("getAllLeagues", leagueRepository.findAll());
            return "/league/getLeagues";
        }
        archiveSeason(league);
        resetLeagueForNewSeason(league);
        model.addAttribute("message", "New season started successfully!");
        model.addAttribute("getAllLeagues", leagueRepository.findAll());
        return "/league/getLeagues";
    }

    private void archiveSeason(League league) {
        Season season = new Season();
        season.setLeague(league);
        Integer currentSeason = league.getCurrentSeason();
        if (currentSeason == null || currentSeason < 1) {
            currentSeason = 1;
        }
        season.setSeasonNumber(currentSeason);
        season.setFinishedAt(LocalDateTime.now());
        season.setStatus(Status.FINISHED);
        List<Standing> standings = league.getStandings();
        if (standings != null && !standings.isEmpty()) {
            List<Standing> sortedStandings = standings.stream()
                    .sorted((s1, s2) -> {
                        int pointsComparison = Integer.compare(
                                s2.getPoints() == null ? 0 : s2.getPoints(),
                                s1.getPoints() == null ? 0 : s1.getPoints());
                        if (pointsComparison != 0) {
                            return pointsComparison;
                        }
                        int s1Goals = s1.getScoredGoals() == null ? 0 : s1.getScoredGoals();
                        int s2Goals = s2.getScoredGoals() == null ? 0 : s2.getScoredGoals();
                        return Integer.compare(s2Goals, s1Goals);
                    })
                    .collect(Collectors.toList());
            List<SeasonStanding> seasonStandings = new ArrayList<>();
            int position = 1;
            for (Standing standing : sortedStandings) {
                SeasonStanding seasonStanding = new SeasonStanding();
                seasonStanding.setSeason(season);
                seasonStanding.setFootballTeam(standing.getFootballTeam());
                seasonStanding.setPoints(standing.getPoints() == null ? 0 : standing.getPoints());
                seasonStanding.setPlayedMatches(standing.getPlayedMatches() == null ? 0 : standing.getPlayedMatches());
                seasonStanding.setScoredGoals(standing.getScoredGoals() == null ? 0 : standing.getScoredGoals());
                seasonStanding.setConcededGoals(standing.getConcededGoals() == null ? 0 : standing.getConcededGoals());
                seasonStanding.setPosition(position++);
                seasonStandings.add(seasonStanding);
            }
            season.setStandings(seasonStandings);
        }
        seasonRepository.save(season);
    }

    private void resetLeagueForNewSeason(League league) {
        clearSchedule(league);
        resetStandings(league);
        Integer currentSeason = league.getCurrentSeason();
        if (currentSeason == null) {
            currentSeason = 1;
        }
        league.setCurrentSeason(currentSeason + 1);
        GameWeekManager gameWeekManager = new GameWeekManager();
        List<GameWeek> gameWeekList = gameWeekManager.generateGameWeeks(league);
        league.setGameWeekList(gameWeekList);
        for (GameWeek gameWeek : gameWeekList) {
            gameWeekRepository.save(gameWeek);
            List<FootballMatch> matchList = gameWeek.getMatchList();
            if (matchList != null) {
                for (FootballMatch match : matchList) {
                    footballMatchRepository.save(match);
                }
            }
        }
        league.setLeagueStatus(Status.STARTED);
        leagueRepository.save(league);
    }

    private void clearSchedule(League league) {
        List<GameWeek> gameWeeks = league.getGameWeekList();
        if (gameWeeks == null) {
            return;
        }
        List<GameWeek> copyGameWeeks = new ArrayList<>(gameWeeks);
        for (GameWeek gameWeek : copyGameWeeks) {
            List<FootballMatch> matches = gameWeek.getMatchList();
            if (matches != null && !matches.isEmpty()) {
                List<FootballMatch> copyMatches = new ArrayList<>(matches);
                for (FootballMatch match : copyMatches) {
                    match.setGameWeek(null);
                }
                footballMatchRepository.deleteAll(copyMatches);
            }
        }
        for (GameWeek gameWeek : copyGameWeeks) {
            gameWeek.setMatchList(new ArrayList<>());
            gameWeek.setLeague(null);
        }
        gameWeekRepository.deleteAll(copyGameWeeks);
        league.setGameWeekList(new ArrayList<>());
    }

    private void resetStandings(League league) {
        List<Standing> standings = league.getStandings();
        if (standings == null) {
            return;
        }
        for (Standing standing : standings) {
            standing.setPoints((byte) 0);
            standing.setScoredGoals((short) 0);
            standing.setConcededGoals((short) 0);
            standing.setPlayedMatches((byte) 0);
            standingRepository.save(standing);
        }
    }
}
