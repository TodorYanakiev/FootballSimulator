package com.example.FootballSimulator.FootballMatch;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.Constants.Position;
import com.example.FootballSimulator.Constants.Status;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.FootballTeam.FootballTeamRepository;
import com.example.FootballSimulator.GameWeek.GameWeekRepository;
import com.example.FootballSimulator.LineUp.LineUp;
import com.example.FootballSimulator.Standings.Standing;
import com.example.FootballSimulator.Standings.StandingRepository;
import com.example.FootballSimulator.TransferSumCalculator;
import com.example.FootballSimulator.User.User;
import com.example.FootballSimulator.User.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class FootballMatchService {
    private FootballMatchRepository footballMatchRepository;

    private StandingRepository standingsRepository;

    private UserRepository userRepository;

    private FootballTeamRepository footballTeamRepository;

    private GameWeekRepository gameWeekRepository;

    public FootballMatchService(FootballMatchRepository footballMatchRepository, StandingRepository standingsRepository,
                                UserRepository userRepository, FootballTeamRepository footballTeamRepository, GameWeekRepository gameWeekRepository) {
        this.footballMatchRepository = footballMatchRepository;
        this.standingsRepository = standingsRepository;
        this.userRepository = userRepository;
        this.footballTeamRepository = footballTeamRepository;
        this.gameWeekRepository = gameWeekRepository;
    }

    public String viewMatch(Long matchId, Model model) {
        Optional<FootballMatch> optionalFootballMatch = footballMatchRepository.findById(matchId);
        if (optionalFootballMatch.isPresent()) {
            model.addAttribute("footballMatch", optionalFootballMatch.get());
            return "/football-match/view";
        }
        model.addAttribute("message", "No such match!");
        return "/home";
    }

    public void simulateNonUserMatches(List<FootballMatch> matchList) {
        for (FootballMatch footballMatch : matchList) {
            footballMatch.setMatchStatus(Status.STARTED);
            for (int i = 0; i < 6; i++) {
                simulate15Minutes(footballMatch);
            }
            updateMatchAftermath(footballMatch);
            saveMatchResults(footballMatch);
            footballMatch.setMatchStatus(Status.FINISHED);
            footballMatchRepository.save(footballMatch);
        }
    }

    public String simulateUserMatch(Long matchId, byte matchPart, Model model) {
        Optional<FootballMatch> optionalFootballMatch = footballMatchRepository.findById(matchId);
        if (optionalFootballMatch.isEmpty()) {
            model.addAttribute("message", "Error finding the match");
            return "/home";
        }
        FootballMatch footballMatch = optionalFootballMatch.get();
        if (!footballMatch.getMatchStatus().equals(Status.STARTED)) {
            model.addAttribute("message", "Error, the match is finished");
            return "/home";
        }
        if (matchPart < 0 || matchPart > 5){
            model.addAttribute("message", "Error, invalid match part.");
            return "/home";
        }
        if (matchPart == 5) {
            updateMatchAftermath(footballMatch);
            footballMatch.setMatchStatus(Status.FINISHED);
            footballMatchRepository.save(footballMatch);
            return "redirect:/game-week/all/" + footballMatch.getGameWeek().getLeague().getId();
        }
        simulate15Minutes(footballMatch);
        saveMatchResults(footballMatch);
        matchPart++;
        model.addAttribute("footballMatch", footballMatch);
        model.addAttribute("matchPart", matchPart);
        model.addAttribute("subs", getSubInPlayers());
        model.addAttribute("players", getSubOutPlayers());
        return "/football-match/play";
    }

    private List<FootballPlayer> getSubInPlayers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(authentication.getName());
        FootballTeam usersFootballTeam = user.getFootballTeam();
        List<FootballPlayer> subs = usersFootballTeam.getPlayerList();
        Map<Position, FootballPlayer> positionFootballPlayerMap = usersFootballTeam.getLineUp().getPositionFootballPlayerMap();
        for(Map.Entry<Position, FootballPlayer> entry: positionFootballPlayerMap.entrySet()) {
            subs.remove(entry.getValue());
        }
        return subs;
    }

    private List<FootballPlayer> getSubOutPlayers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(authentication.getName());
        FootballTeam usersFootballTeam = user.getFootballTeam();
        Map<Position, FootballPlayer> positionFootballPlayerMap = usersFootballTeam.getLineUp().getPositionFootballPlayerMap();
        List<FootballPlayer> subs = new ArrayList<>();
        for(Map.Entry<Position, FootballPlayer> entry : positionFootballPlayerMap.entrySet()) {
            subs.add(entry.getValue());
        }
        return subs;
    }

    private void simulate15Minutes(FootballMatch footballMatch) {
        FootballTeam homeTeam = footballMatch.getHomeTeam();
        FootballTeam awayTeam = footballMatch.getAwayTeam();
        LineUp homeLineUp = homeTeam.getLineUp();
        LineUp awayLineUp = awayTeam.getLineUp();
        Map<Position, FootballPlayer> homeAttackLine = getAttackLine(homeLineUp);
        Map<Position, FootballPlayer> awayAttackLine = getAttackLine(awayLineUp);
        Map<Position, FootballPlayer> homeMidfieldLine = getMidfieldLine(homeLineUp);
        Map<Position, FootballPlayer> awayMidfieldLine = getMidfieldLine(awayLineUp);
        Map<Position, FootballPlayer> homeDefenseLine = getDefenseLine(homeLineUp);
        Map<Position, FootballPlayer> awayDefenseLine = getDefenseLine(awayLineUp);
        Map<Position, FootballPlayer> homeGoalkeeper = getGoalkeeper(homeLineUp);
        Map<Position, FootballPlayer> awayGoalkeeper = getGoalkeeper(awayLineUp);
        for (int i = 0; i < 15; i++) {
            simulateAttack(footballMatch, homeAttackLine, awayAttackLine, homeMidfieldLine, awayMidfieldLine, homeDefenseLine,
                    awayDefenseLine, homeGoalkeeper, awayGoalkeeper);
        }
    }

    private void simulateAttack(FootballMatch footballMatch, Map<Position, FootballPlayer> homeAttackLine, Map<Position, FootballPlayer> awayAttackLine, Map<Position, FootballPlayer> homeMidfieldLine,
                                Map<Position, FootballPlayer> awayMidfieldLine, Map<Position, FootballPlayer> homeDefenseLine, Map<Position, FootballPlayer> awayDefenseLine,
                                Map<Position, FootballPlayer> homeGoalkeeper, Map<Position, FootballPlayer> awayGoalkeeper) {
        double homeAttackPower = calculateLinePower(homeAttackLine);
        double awayAttackPower = calculateLinePower(awayAttackLine);
        double homeMidfieldPower = calculateLinePower(homeMidfieldLine);
        double awayMidfieldPower = calculateLinePower(awayMidfieldLine);
        double homeDefensePower = calculateLinePower(homeDefenseLine);
        double awayDefensePower = calculateLinePower(awayDefenseLine);
        double homeGoalkeeperPower = calculateLinePower(homeGoalkeeper);
        double awayGoalkeeperPower = calculateLinePower(awayGoalkeeper);
        if (homeMidfieldPower / homeMidfieldLine.size() > (awayMidfieldPower / awayMidfieldLine.size()) * 1.01) {
            if (homeMidfieldPower / homeMidfieldLine.size() > (awayDefensePower / awayDefenseLine.size()) * 1.05) {//1.015
                if (homeAttackPower / homeAttackLine.size() > (awayDefensePower / awayDefenseLine.size()) * 1.15) {//1.017, 1.1
                    if (homeAttackPower / homeAttackLine.size() > awayGoalkeeperPower * 1.4) {
                        footballMatch.setHomeTeamScore((byte) (footballMatch.getHomeTeamScore() + 1));
                    }
                    footballMatch.setHomeShots((byte) (footballMatch.getHomeShots() + 1));
                }
                footballMatch.setDangerHomeAttacks((byte) (footballMatch.getDangerHomeAttacks() + 1));
            }
            footballMatch.setHomeAttacks((byte) (footballMatch.getHomeAttacks() + 1));
        } else if (awayMidfieldPower / awayMidfieldLine.size() > (homeMidfieldPower / homeMidfieldLine.size()) * 1.01) {
            if (awayMidfieldPower / awayMidfieldLine.size() > (homeDefensePower / homeDefenseLine.size()) * 1.05) {
                if (awayAttackPower / awayAttackLine.size() > (homeDefensePower / homeDefenseLine.size()) * 1.15) {
                    if (awayAttackPower / awayAttackLine.size() > homeGoalkeeperPower * 1.4) { //1.05
                        footballMatch.setAwayTeamScore((byte) (footballMatch.getAwayTeamScore() + 1));
                    }
                    footballMatch.setAwayShots((byte) (footballMatch.getAwayShots() + 1));
                }
                footballMatch.setDangerAwayAttacks((byte) (footballMatch.getDangerAwayAttacks() + 1));
            }
            footballMatch.setAwayAttacks((byte) (footballMatch.getAwayAttacks() + 1));
        }
    }

    private double calculateLinePower(Map<Position, FootballPlayer> line) {
        long seed = System.nanoTime();
        Random random = new Random(seed);
        double power = 0;
        for (Map.Entry<Position, FootballPlayer> entry : line.entrySet()) {
            double playerPower = getStartPlayerPower(entry.getValue(), entry.getKey());
            double modifier = random.nextDouble(0.5, 2.11) + getPreferredPositionModifier(entry.getValue(), entry.getKey());
            power += playerPower * modifier;
        }
        return power;
    }

    private void updateMatchAftermath(FootballMatch footballMatch) {
        Standing homeStanding = footballMatch.getHomeTeam().getStanding();
        Standing awayStanding = footballMatch.getAwayTeam().getStanding();
        FootballTeam homeTeam = footballMatch.getHomeTeam();
        FootballTeam awayTeam = footballMatch.getAwayTeam();
        if (footballMatch.getHomeTeamScore().equals(footballMatch.getAwayTeamScore())) {
            homeStanding.setPoints((byte) (homeStanding.getPoints() + 1));
            awayStanding.setPoints((byte) (awayStanding.getPoints() + 1));
            homeTeam.setBudged(homeTeam.getBudged() + 5000000);
            awayTeam.setBudged(awayTeam.getBudged() + 5000000);
        } else if (footballMatch.getHomeTeamScore() > footballMatch.getAwayTeamScore()) {
            homeStanding.setPoints((byte) (homeStanding.getPoints() + 3));
            homeTeam.setBudged(homeTeam.getBudged() + 10000000);
            awayTeam.setBudged(awayTeam.getBudged() + 2500000);
        } else {
            awayStanding.setPoints((byte) (awayStanding.getPoints() + 3));
            homeTeam.setBudged(homeTeam.getBudged() + 2500000);
            awayTeam.setBudged(awayTeam.getBudged() + 10000000);
        }
        homeStanding.setPlayedMatches((byte) (homeStanding.getPlayedMatches() + 1));
        homeStanding.setScoredGoals((short) (homeStanding.getScoredGoals() + footballMatch.getHomeTeamScore()));
        homeStanding.setConcededGoals((short) (homeStanding.getConcededGoals() + footballMatch.getAwayTeamScore()));
        awayStanding.setPlayedMatches((byte) (awayStanding.getPlayedMatches() + 1));
        awayStanding.setScoredGoals((short) (awayStanding.getScoredGoals() + footballMatch.getAwayTeamScore()));
        awayStanding.setConcededGoals((short) (awayStanding.getConcededGoals() + footballMatch.getHomeTeamScore()));
        standingsRepository.save(homeStanding);
        standingsRepository.save(awayStanding);
        footballTeamRepository.save(homeTeam);
        footballTeamRepository.save(awayTeam);
    }

    private void saveMatchResults(FootballMatch footballMatch) {
        footballMatchRepository.save(footballMatch);
        standingsRepository.save(footballMatch.getHomeTeam().getStanding());
        standingsRepository.save(footballMatch.getAwayTeam().getStanding());
    }

    private Map<Position, FootballPlayer> getMidfieldLine(LineUp lineUp) {
        Map<Position, FootballPlayer> midfieldLineMap = new HashMap<>();
        Map<Position, FootballPlayer> positionFootballPlayerMap = lineUp.getPositionFootballPlayerMap();
        for (Map.Entry<Position, FootballPlayer> entry : positionFootballPlayerMap.entrySet()) {
            Position position = entry.getKey();
            if (position.equals(Position.CM) || position.equals(Position.LCM) || position.equals(Position.RCM) ||
                    position.equals(Position.LM) || position.equals(Position.RM)) {
                midfieldLineMap.put(entry.getKey(), entry.getValue());
            }
        }
        return midfieldLineMap;
    }

    private Map<Position, FootballPlayer> getAttackLine(LineUp lineUp) {
        Map<Position, FootballPlayer> attackLineMap = new HashMap<>();
        Map<Position, FootballPlayer> positionFootballPlayerMap = lineUp.getPositionFootballPlayerMap();
        for (Map.Entry<Position, FootballPlayer> entry : positionFootballPlayerMap.entrySet()) {
            Position position = entry.getKey();
            if (position.equals(Position.CF) || position.equals(Position.LCF) || position.equals(Position.RCF) ||
                    position.equals(Position.LF) || position.equals(Position.RF)) {
                attackLineMap.put(entry.getKey(), entry.getValue());
            }
        }
        return attackLineMap;
    }

    private Map<Position, FootballPlayer> getDefenseLine(LineUp lineUp) {
        Map<Position, FootballPlayer> defenseLineMap = new HashMap<>();
        Map<Position, FootballPlayer> positionFootballPlayerMap = lineUp.getPositionFootballPlayerMap();
        for (Map.Entry<Position, FootballPlayer> entry : positionFootballPlayerMap.entrySet()) {
            Position position = entry.getKey();
            if (position.equals(Position.CB) || position.equals(Position.LCB) || position.equals(Position.RCB) ||
                    position.equals(Position.LB) || position.equals(Position.RB)) {
                defenseLineMap.put(entry.getKey(), entry.getValue());
            }
        }
        return defenseLineMap;
    }

    private Map<Position, FootballPlayer> getGoalkeeper(LineUp lineUp) {
        Map<Position, FootballPlayer> goalkeeperMap = new HashMap<>();
        Map<Position, FootballPlayer> positionFootballPlayerMap = lineUp.getPositionFootballPlayerMap();
        for (Map.Entry<Position, FootballPlayer> entry : positionFootballPlayerMap.entrySet()) {
            Position position = entry.getKey();
            if (position.equals(Position.GK)) {
                goalkeeperMap.put(entry.getKey(), entry.getValue());
            }
        }
        return goalkeeperMap;
    }

    private double getStartPlayerPower(FootballPlayer player, Position position) {
        TransferSumCalculator transferSumCalculator = new TransferSumCalculator();
        return transferSumCalculator.calculatePlayerOverall(player, position);
    }

    private double getPreferredPositionModifier(FootballPlayer player, Position position) {
        //if player plays on its preferred position gets higher modifier
        long seed = System.nanoTime();
        Random random = new Random(seed);
        BaseFootballPlayer baseFootballPlayer = player.getBaseFootballPlayer();
        Position prefPosition = baseFootballPlayer.getPosition();
        String preferredLine = prefPosition.getLabel().substring(prefPosition.getLabel().length() - 1, prefPosition.getLabel().length() - 1);
        String line = position.getLabel().substring(position.getLabel().length() - 1, position.getLabel().length() - 1);
        if (prefPosition.equals(position)) return random.nextDouble(1.01, 1.06);
        if (preferredLine.equals(line)) return 1;
        return random.nextDouble(0.95, 1.00);
    }
}
