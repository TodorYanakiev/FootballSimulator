package com.example.FootballSimulator.FootballMatch;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.Constants.Position;
import com.example.FootballSimulator.Constants.Status;
import com.example.FootballSimulator.Constants.TeamFormation;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.League.League;
import com.example.FootballSimulator.LineUp.LineUp;
import com.example.FootballSimulator.Standings.Standing;
import com.example.FootballSimulator.Standings.StandingRepository;
import com.example.FootballSimulator.Standings.StandingsService;
import com.example.FootballSimulator.TransferSumCalculator;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class FootballMatchService {
    private FootballMatchRepository footballMatchRepository;

    private StandingRepository standingsRepository;

    public FootballMatchService(FootballMatchRepository footballMatchRepository, StandingRepository standingsRepository) {
        this.footballMatchRepository = footballMatchRepository;
        this.standingsRepository = standingsRepository;
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
            FootballTeam homeTeam = footballMatch.getHomeTeam();
            FootballTeam awayTeam = footballMatch.getAwayTeam();
            Standing homeStanding = homeTeam.getStanding();
            Standing awayStanding = awayTeam.getStanding();
            footballMatch.setMatchStatus(Status.STARTED);
            for (int i = 0; i < 6; i++) {
                simulate15Minutes(footballMatch);
            }
            if (footballMatch.getHomeTeamScore().equals(footballMatch.getAwayTeamScore())) {
                homeStanding.setPoints((byte) (homeStanding.getPoints() + 1));
                awayStanding.setPoints((byte) (awayStanding.getPoints() + 1));
            } else if (footballMatch.getHomeTeamScore() > footballMatch.getAwayTeamScore()) {
                homeStanding.setPoints((byte) (homeStanding.getPoints() + 3));
            } else {
                awayStanding.setPoints((byte) (awayStanding.getPoints() + 3));
            }
            homeStanding.setPlayedMatches((byte) (homeStanding.getPlayedMatches() + 1));
            homeStanding.setScoredGoals((short) (homeStanding.getScoredGoals() + footballMatch.getHomeTeamScore()));
            homeStanding.setConcededGoals((short) (homeStanding.getConcededGoals() + footballMatch.getAwayTeamScore()));
            awayStanding.setPlayedMatches((byte) (awayStanding.getPlayedMatches() + 1));
            awayStanding.setScoredGoals((short) (awayStanding.getScoredGoals() + footballMatch.getAwayTeamScore()));
            awayStanding.setConcededGoals((short) (awayStanding.getConcededGoals() + footballMatch.getHomeTeamScore()));
            footballMatch.setMatchStatus(Status.FINISHED);
            footballMatchRepository.save(footballMatch);
            standingsRepository.save(homeStanding);
            standingsRepository.save(awayStanding);
        }
    }

//    private void simulate15Minutes(FootballMatch footballMatch) {
//        FootballTeam homeTeam = footballMatch.getHomeTeam();
//        FootballTeam awayTeam = footballMatch.getAwayTeam();
//        LineUp homeLineUp = homeTeam.getLineUp();
//        LineUp awayLineUp = awayTeam.getLineUp();
//        Map<Position, FootballPlayer> homeAttackLine = getAttackLine(homeLineUp);
//        Map<Position, FootballPlayer> awayAttackLine = getAttackLine(awayLineUp);
//        Map<Position, FootballPlayer> homeMidfieldLine = getMidfieldLine(homeLineUp);
//        Map<Position, FootballPlayer> awayMidfieldLine = getMidfieldLine(awayLineUp);
//        Map<Position, FootballPlayer> homeDefenseLine = getDefenseLine(homeLineUp);
//        Map<Position, FootballPlayer> awayDefenseLine = getDefenseLine(awayLineUp);
//        Map<Position, FootballPlayer> homeGoalkeeper = getGoalkeeper(homeLineUp);
//        Map<Position, FootballPlayer> awayGoalkeeper = getGoalkeeper(awayLineUp);
//        double homeAttackPower = 1;
//        double awayAttackPower = 1;
//        double homeMidfieldPower = 1;
//        double awayMidfieldPower = 1;
//        double homeDefensePower = 1;
//        double awayDefensePower = 1;
//        double homeGoalkeeperPower = 1;
//        double awayGoalkeeperPower = 1;
//
//        for (int i = 0; i < 15; i++) {
//            homeAttackPower = 0;
//            awayAttackPower = 0;
//            homeMidfieldPower = 0;
//            awayMidfieldPower = 0;
//            homeDefensePower = 0;
//            awayDefensePower = 0;
//            homeGoalkeeperPower = 0;
//            awayGoalkeeperPower = 0;
//            for (Map.Entry<Position, FootballPlayer> entry : homeAttackLine.entrySet()) {
//                long seed = System.nanoTime();
//                Random random = new Random(seed);
//                homeAttackPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
//                        (random.nextDouble(0.20, 10.51) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
//            }
//            for (Map.Entry<Position, FootballPlayer> entry : awayAttackLine.entrySet()) {
//                long seed = System.nanoTime();
//                Random random = new Random(seed);
//                awayAttackPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
//                        (random.nextDouble(0.20, 10.51) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
//            }
//            for (Map.Entry<Position, FootballPlayer> entry : homeMidfieldLine.entrySet()) {
//                long seed = System.nanoTime();
//                Random random = new Random(seed);
//                homeMidfieldPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
//                        (random.nextDouble(0.20, 10.51) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
//            }
//            for (Map.Entry<Position, FootballPlayer> entry : awayMidfieldLine.entrySet()) {
//                long seed = System.nanoTime();
//                Random random = new Random(seed);
//                awayMidfieldPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
//                        (random.nextDouble(0.20, 10.51) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
//            }
//            for (Map.Entry<Position, FootballPlayer> entry : homeDefenseLine.entrySet()) {
//                long seed = System.nanoTime();
//                Random random = new Random(seed);
//                homeDefensePower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
//                        (random.nextDouble(0.20, 10.51) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
//            }
//            for (Map.Entry<Position, FootballPlayer> entry : awayDefenseLine.entrySet()) {
//                long seed = System.nanoTime();
//                Random random = new Random(seed);
//                awayDefensePower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
//                        (random.nextDouble(0.20, 10.51) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
//            }
//            for (Map.Entry<Position, FootballPlayer> entry : homeGoalkeeper.entrySet()) {
//                long seed = System.nanoTime();
//                Random random = new Random(seed);
//                homeGoalkeeperPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
//                        (random.nextDouble(2, 10.51) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
//            }
//            for (Map.Entry<Position, FootballPlayer> entry : awayGoalkeeper.entrySet()) {
//                long seed = System.nanoTime();
//                Random random = new Random(seed);
//                awayGoalkeeperPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
//                        (random.nextDouble(2, 10.51) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
//            }
//            if (homeMidfieldPower / homeMidfieldLine.size() > awayMidfieldPower / awayMidfieldLine.size()) {
//                if (homeMidfieldPower / homeMidfieldLine.size() > (awayDefensePower / awayDefenseLine.size()) * 1.05) {
//                    if (homeAttackPower / homeAttackLine.size() > (awayDefensePower / awayDefenseLine.size()) * 1.09) {
//                        if (homeAttackPower / homeAttackLine.size() > awayGoalkeeperPower * 1.5) {
//                            footballMatch.setHomeTeamScore((byte) (footballMatch.getHomeTeamScore() + 1));
//                            //TODO make logic for scorers
//                        }
//                        footballMatch.setHomeShots((byte) (footballMatch.getHomeShots() + 1));
//                    }
//                    footballMatch.setDangerHomeAttacks((byte) (footballMatch.getDangerHomeAttacks() + 1));
//                }
//                footballMatch.setHomeAttacks((byte) (footballMatch.getHomeAttacks() + 1));
//            } else if (awayMidfieldPower / awayMidfieldLine.size() > homeMidfieldPower / homeMidfieldLine.size()) {
//                if (awayMidfieldPower / awayMidfieldLine.size() > (homeDefensePower / homeDefenseLine.size()) * 1.05) {
//                    if (awayAttackPower / awayAttackLine.size() > (homeDefensePower / homeDefenseLine.size()) * 1.09) {
//                        if (awayAttackPower / awayAttackLine.size() > homeGoalkeeperPower * 1.5) {
//                            footballMatch.setAwayTeamScore((byte) (footballMatch.getAwayTeamScore() + 1));
//                        }
//                        footballMatch.setAwayShots((byte) (footballMatch.getAwayShots() + 1));
//                    }
//                    footballMatch.setDangerAwayAttacks((byte) (footballMatch.getDangerAwayAttacks() + 1));
//                }
//                footballMatch.setAwayAttacks((byte) (footballMatch.getAwayAttacks() + 1));
//            }
//        }
//    }
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
    double homeAttackPower = 1;
    double awayAttackPower = 1;
    double homeMidfieldPower = 1;
    double awayMidfieldPower = 1;
    double homeDefensePower = 1;
    double awayDefensePower = 1;
    double homeGoalkeeperPower = 1;
    double awayGoalkeeperPower = 1;

    long seed = System.nanoTime();
    Random random = new Random(seed);
    for (int i = 0; i < 15; i++) {
        homeAttackPower = 0;
        awayAttackPower = 0;
        homeMidfieldPower = 0;
        awayMidfieldPower = 0;
        homeDefensePower = 0;
        awayDefensePower = 0;
        homeGoalkeeperPower = 0;
        awayGoalkeeperPower = 0;
        for (Map.Entry<Position, FootballPlayer> entry : homeAttackLine.entrySet()) {
            homeAttackPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
                    (random.nextDouble(0.9, 1.11) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
        }
        for (Map.Entry<Position, FootballPlayer> entry : awayAttackLine.entrySet()) {
            awayAttackPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
                    (random.nextDouble(0.9, 1.11) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
        }
        for (Map.Entry<Position, FootballPlayer> entry : homeMidfieldLine.entrySet()) {
            homeMidfieldPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
                    (random.nextDouble(0.9, 1.11) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
        }
        for (Map.Entry<Position, FootballPlayer> entry : awayMidfieldLine.entrySet()) {
            awayMidfieldPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
                    (random.nextDouble(0.9, 1.11) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
        }
        for (Map.Entry<Position, FootballPlayer> entry : homeDefenseLine.entrySet()) {
            homeDefensePower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
                    (random.nextDouble(0.9, 1.11) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
        }
        for (Map.Entry<Position, FootballPlayer> entry : awayDefenseLine.entrySet()) {
            awayDefensePower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
                    (random.nextDouble(0.9, 1.11) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
        }
        for (Map.Entry<Position, FootballPlayer> entry : homeGoalkeeper.entrySet()) {
            homeGoalkeeperPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
                    (random.nextDouble(0.9, 1.11) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
        }
        for (Map.Entry<Position, FootballPlayer> entry : awayGoalkeeper.entrySet()) {
            awayGoalkeeperPower += getStartPlayerPower(entry.getValue(), entry.getKey()) *
                    (random.nextDouble(0.9, 1.11) + getPreferredPositionModifier(entry.getValue(), entry.getKey()));
        }
        if (homeMidfieldPower / homeMidfieldLine.size() > awayMidfieldPower / awayMidfieldLine.size()) {
            if (homeMidfieldPower / homeMidfieldLine.size() > (awayDefensePower / awayDefenseLine.size()) * 1.015) {
                if (homeAttackPower / homeAttackLine.size() > (awayDefensePower / awayDefenseLine.size()) * 1.017) {
                    if (homeAttackPower / homeAttackLine.size() > awayGoalkeeperPower * 1.05) {
                        footballMatch.setHomeTeamScore((byte) (footballMatch.getHomeTeamScore() + 1));
                        //TODO make logic for scorers
                    }
                    footballMatch.setHomeShots((byte) (footballMatch.getHomeShots() + 1));
                }
                footballMatch.setDangerHomeAttacks((byte) (footballMatch.getDangerHomeAttacks() + 1));
            }
            footballMatch.setHomeAttacks((byte) (footballMatch.getHomeAttacks() + 1));
        } else if (awayMidfieldPower / awayMidfieldLine.size() > homeMidfieldPower / homeMidfieldLine.size()) {
            if (awayMidfieldPower / awayMidfieldLine.size() > (homeDefensePower / homeDefenseLine.size()) * 1.015) {
                if (awayAttackPower / awayAttackLine.size() > (homeDefensePower / homeDefenseLine.size()) * 1.017) {
                    if (awayAttackPower / awayAttackLine.size() > homeGoalkeeperPower * 1.05) {
                        footballMatch.setAwayTeamScore((byte) (footballMatch.getAwayTeamScore() + 1));
                    }
                    footballMatch.setAwayShots((byte) (footballMatch.getAwayShots() + 1));
                }
                footballMatch.setDangerAwayAttacks((byte) (footballMatch.getDangerAwayAttacks() + 1));
            }
            footballMatch.setAwayAttacks((byte) (footballMatch.getAwayAttacks() + 1));
        }
    }
}

//    private List<FootballPlayer> getMidfielderLine(LineUp lineUp) {
//        List<FootballPlayer> footballPlayers = new ArrayList<>();
//        Map<Position, FootballPlayer> positionFootballPlayerMap = lineUp.getPositionFootballPlayerMap();
//        for (Map.Entry<Position, FootballPlayer> entry : positionFootballPlayerMap.entrySet()) {
//            Position position = entry.getKey();
//            if (position.equals(Position.CM) || position.equals(Position.LCM) || position.equals(Position.RCM) ||
//                    position.equals(Position.LM) || position.equals(Position.RM)) {
//                footballPlayers.add(entry.getValue());
//            }
//        }
//        return footballPlayers;
//    }

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

//    private List<FootballPlayer> getAttackLine(LineUp lineUp) {
//        List<FootballPlayer> footballPlayers = new ArrayList<>();
//        Map<Position, FootballPlayer> positionFootballPlayerMap = lineUp.getPositionFootballPlayerMap();
//        for (Map.Entry<Position, FootballPlayer> entry : positionFootballPlayerMap.entrySet()) {
//            Position position = entry.getKey();
//            if (position.equals(Position.CF) || position.equals(Position.LCF) || position.equals(Position.RCF) ||
//                    position.equals(Position.LF) || position.equals(Position.RF)) {
//                footballPlayers.add(entry.getValue());
//            }
//        }
//        return footballPlayers;
//    }

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

//    private List<FootballPlayer> getDefenseLine(LineUp lineUp) {
//        List<FootballPlayer> footballPlayers = new ArrayList<>();
//        Map<Position, FootballPlayer> positionFootballPlayerMap = lineUp.getPositionFootballPlayerMap();
//        for (Map.Entry<Position, FootballPlayer> entry : positionFootballPlayerMap.entrySet()) {
//            Position position = entry.getKey();
//            if (position.equals(Position.CB) || position.equals(Position.LCB) || position.equals(Position.RCB) ||
//                    position.equals(Position.LB) || position.equals(Position.RB)) {
//                footballPlayers.add(entry.getValue());
//            }
//        }
//        return footballPlayers;
//    }

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
        //return transferSumCalculator.calculatePlayerOverall(player, position);
        if (position.equals(Position.CM) || position.equals(Position.RCM) || position.equals(Position.LCM)) {
            return player.getSpeed() * 0.2 + player.getPassing() * 0.4 + player.getPositioning() * 0.4;
        } else if (position.equals(Position.LM) || position.equals(Position.RM)) {
            return player.getSpeed() * 0.3 + player.getPassing() * 0.35 + player.getPositioning() * 0.35;
        } else if (position.equals(Position.LB) || position.equals(Position.RB)) {
            return player.getSpeed() * 0.3 + player.getDefending() * 0.35 + player.getPositioning() * 0.35;
        } else if (position.equals(Position.CB) || position.equals(Position.LCB) || position.equals(Position.RCB)) {
            return player.getSpeed() * 0.15 + player.getDefending() * 0.45 + player.getPositioning() * 0.4;
        } else if (position.equals(Position.CF) || position.equals(Position.LCF) || position.equals(Position.RCF)) {
            return player.getPassing() * 0.1 + player.getSpeed() * 0.2 + player.getScoring() * 0.3 +
                    player.getDribble() * 0.2 + player.getPositioning() * 0.2;
        } else if (position.equals(Position.LF) || position.equals(Position.RF)) {
            return player.getSpeed() * 0.2 + player.getDribble() * 0.2 + player.getPassing() * 0.25 + player.getPositioning() * 0.2
                    + player.getScoring() * 0.15;
        } else {
            return player.getPositioning() * 0.2 + player.getGoalkeeping() * 0.8;
        }
    }

    private double getPreferredPositionModifier(FootballPlayer player, Position position) {
        //if player plays on its preferred position gets higher modifier
        long seed = System.nanoTime();
        Random random = new Random(seed);
        BaseFootballPlayer baseFootballPlayer = player.getBaseFootballPlayer();
        Position prefPosition = baseFootballPlayer.getPosition();
        String preferredLine = prefPosition.getLabel().substring(prefPosition.getLabel().length() - 1, prefPosition.getLabel().length() - 1);
        String line = position.getLabel().substring(position.getLabel().length() - 1, position.getLabel().length() - 1);
        if (prefPosition.equals(position)) return random.nextDouble(1.1, 1.21);
        if (preferredLine.equals(line)) return 1;
        return random.nextDouble(0.80, 0.91);
    }

    /*private double getStaminaModifier(FootballPlayer player, int partOfMatch) {
        //TODO
        return 1;
    }*/
}
