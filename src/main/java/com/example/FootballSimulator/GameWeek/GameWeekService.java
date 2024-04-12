package com.example.FootballSimulator.GameWeek;

import com.example.FootballSimulator.Constants.Position;
import com.example.FootballSimulator.Constants.Status;
import com.example.FootballSimulator.FootballMatch.FootballMatch;
import com.example.FootballSimulator.FootballMatch.FootballMatchRepository;
import com.example.FootballSimulator.FootballMatch.FootballMatchService;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballPlayer.FootballPlayerRepository;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.League.League;
import com.example.FootballSimulator.LineUp.LineUp;
import com.example.FootballSimulator.LineUp.LineUpRepository;
import com.example.FootballSimulator.User.User;
import com.example.FootballSimulator.User.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameWeekService {
    private GameWeekRepository gameWeekRepository;

    private FootballMatchRepository footballMatchRepository;

    private UserRepository userRepository;

    private FootballMatchService footballMatchService;

    private FootballPlayerRepository footballPlayerRepository;
    private final LineUpRepository lineUpRepository;

    public GameWeekService(GameWeekRepository gameWeekRepository, FootballMatchRepository footballMatchRepository,
                           UserRepository userRepository, FootballMatchService footballMatchService,
                           FootballPlayerRepository footballPlayerRepository,
                           LineUpRepository lineUpRepository) {
        this.gameWeekRepository = gameWeekRepository;
        this.footballMatchRepository = footballMatchRepository;
        this.userRepository = userRepository;
        this.footballMatchService = footballMatchService;
        this.footballPlayerRepository = footballPlayerRepository;
        this.lineUpRepository = lineUpRepository;
    }

    public String simulateGameWeek(Long gameWeekId, Model model) {
        Optional<GameWeek> optionalGameWeek = gameWeekRepository.findById(gameWeekId);
        if (optionalGameWeek.isPresent()) {
            GameWeek gameWeekToBePlayed = optionalGameWeek.get();
            if (!checkIfGameWeekIsValid(gameWeekToBePlayed)) {
                model.addAttribute("message", "Invalid game week! All the line-ups must be fulfil!");
                return "/home";
            }
            FootballTeam usersFootballTeam = getUsersFootballTeam();
            FootballMatch userMatch = getUserMatch(gameWeekToBePlayed, usersFootballTeam);
            if (!FootballMatchService.checkIfMatchIsValid(userMatch)) {
                model.addAttribute("message", "Invalid match! The line-ups must be fulfil!");
                return "/home";
            }
            List<FootballMatch> footballMatchesExceptUsers = gameWeekToBePlayed.getMatchList();
            footballMatchesExceptUsers.remove(userMatch);

            footballMatchService.simulateNonUserMatches(footballMatchesExceptUsers);
            gameWeekToBePlayed.setGameWeekStatus(Status.FINISHED);
            gameWeekRepository.save(gameWeekToBePlayed);
            if (userMatch == null) {
                model.addAttribute("message", "Error, no user match found.");
                return "/home";
            }
            userMatch.setMatchStatus(Status.STARTED);
            footballMatchRepository.save(userMatch);
            return footballMatchService.simulateUserMatch(userMatch.getId(), (byte) 0, model);//
        } else {
            model.addAttribute("message", "No such game week");
            return "/home";
        }
    }

    public String continueMatch(Long matchId, byte matchPart, Long subIn, Long subOut, Model model) {
        Optional<FootballMatch> optionalFootballMatch = footballMatchRepository.findById(matchId);
        Optional<FootballPlayer> optionalFootballPlayerIn = footballPlayerRepository.findById(subIn);
        Optional<FootballPlayer> optionalFootballPlayerOut = footballPlayerRepository.findById(subOut);
        if (optionalFootballMatch.isEmpty()) {
            model.addAttribute("message", "Error, invalid ids");
            return "/home";
        }
        if (optionalFootballPlayerIn.isPresent() && optionalFootballPlayerOut.isPresent()) {
            if (subIn != -1 && subOut != -1) {
                FootballPlayer playerIn = optionalFootballPlayerIn.get();
                FootballPlayer playerOut = optionalFootballPlayerOut.get();
                LineUp lineUp = getUsersFootballTeam().getLineUp();
                Map<Position, FootballPlayer> positionFootballPlayerMap = lineUp.getPositionFootballPlayerMap();
                for (Map.Entry<Position, FootballPlayer> entry : positionFootballPlayerMap.entrySet()) {
                    if (entry.getValue().getId() == playerOut.getId()) {
                        positionFootballPlayerMap.replace(entry.getKey(), entry.getValue(), playerIn);
                    }
                }
                lineUp.setPositionFootballPlayerMap(positionFootballPlayerMap);
                lineUpRepository.save(lineUp);
            }
        }
        return footballMatchService.simulateUserMatch(matchId, matchPart, model);
    }

    public FootballTeam getUsersFootballTeam() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(authentication.getName());
        return user.getFootballTeam();
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

    public boolean checkIfGameWeekIsValid(GameWeek gameWeek) {
        if (gameWeek == null || gameWeek.getLeague() == null || gameWeek.getMatchList() == null) return false;
        List<FootballMatch> footballMatchList = gameWeek.getMatchList();
        if (footballMatchList == null) return false;
        for (FootballMatch match : footballMatchList) {
            FootballMatchService.checkIfMatchIsValid(match);
        }
        return true;
    }
}
