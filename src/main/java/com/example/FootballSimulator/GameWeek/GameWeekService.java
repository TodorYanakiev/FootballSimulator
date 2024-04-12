package com.example.FootballSimulator.GameWeek;

import com.example.FootballSimulator.Constants.Status;
import com.example.FootballSimulator.FootballMatch.FootballMatch;
import com.example.FootballSimulator.FootballMatch.FootballMatchRepository;
import com.example.FootballSimulator.FootballMatch.FootballMatchService;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.League.League;
import com.example.FootballSimulator.User.User;
import com.example.FootballSimulator.User.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameWeekService {
    private GameWeekRepository gameWeekRepository;

    private FootballMatchRepository footballMatchRepository;

    private UserRepository userRepository;

    private FootballMatchService footballMatchService;

    public GameWeekService(GameWeekRepository gameWeekRepository, FootballMatchRepository footballMatchRepository,
                           UserRepository userRepository, FootballMatchService footballMatchService) {
        this.gameWeekRepository = gameWeekRepository;
        this.footballMatchRepository = footballMatchRepository;
        this.userRepository = userRepository;
        this.footballMatchService = footballMatchService;
    }

    public String simulateGameWeek(Long gameWeekId, Model model) {
        Optional<GameWeek> optionalGameWeek = gameWeekRepository.findById(gameWeekId);
        if (optionalGameWeek.isPresent()) {
            GameWeek gameWeekToBePlayed = optionalGameWeek.get();
            FootballTeam usersFootballTeam = getUsersFootballTeam();
            FootballMatch userMatch = getUserMatch(gameWeekToBePlayed, usersFootballTeam);
            List<FootballMatch> footballMatchesExceptUsers = gameWeekToBePlayed.getMatchList();
            footballMatchesExceptUsers.remove(userMatch);

            footballMatchService.simulateNonUserMatches(footballMatchesExceptUsers);
            gameWeekToBePlayed.setGameWeekStatus(Status.FINISHED);
            gameWeekRepository.save(gameWeekToBePlayed);
            //TODO non user match
            return footballMatchService.simulateUserMatch(userMatch);
//            model.addAttribute("league", gameWeekToBePlayed.getLeague());
//            return "/game-week/all-for-league";
        } else {
            model.addAttribute("message", "No such game week");
            return "/home";
        }
    }


    private FootballTeam getUsersFootballTeam() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(authentication.getName());
        FootballTeam usersFootballTeam = user.getFootballTeam();
        return usersFootballTeam;
    }

    public String getGameWeekToBePlayed(Model model) {
        FootballTeam usersFootballTeam = getUsersFootballTeam();
        if (usersFootballTeam == null) {
            model.addAttribute("message", "You must select football team!");
            return "/home";
        }
        League league = usersFootballTeam.getLeague();
        List<GameWeek> gameWeekList = league.getGameWeekList();
        List<GameWeek> sortedGameWeekList = gameWeekList.stream().sorted(Comparator.comparing(GameWeek::getWeekNumber))
                .collect(Collectors.toList());
        for (GameWeek gameWeek : sortedGameWeekList) {
            if (gameWeek.getGameWeekStatus().equals(Status.NOT_STARTED)) {
                return "redirect:/game-week/simulate-week/" + gameWeek.getId();
            }
        }
        return null;
    }

    private FootballMatch getUserMatch(GameWeek gameWeek, FootballTeam usersFootballTeam) {
        List<FootballMatch> footballMatchList = gameWeek.getMatchList();
        for (FootballMatch footballMatch : footballMatchList) {
            if (footballMatch.getHomeTeam().getId() == usersFootballTeam.getId() || footballMatch.getAwayTeam().getId() == usersFootballTeam.getId()) {
                return footballMatch;
            }
        }
        return null;
    }
}
