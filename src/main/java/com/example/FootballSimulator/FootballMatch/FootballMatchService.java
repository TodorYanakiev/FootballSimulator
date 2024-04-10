package com.example.FootballSimulator.FootballMatch;

import com.example.FootballSimulator.Constants.Position;
import com.example.FootballSimulator.Constants.TeamFormation;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.LineUp.LineUp;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FootballMatchService {
    public void simulateNonUserMatches(List<FootballMatch> matchList) {
        for (FootballMatch footballMatch : matchList) {
            for (int i = 0; i < 5; i++) {
                simulate15Minutes(footballMatch);
            }
        }
    }

    private void simulate15Minutes(FootballMatch footballMatch) {
        FootballTeam homeTeam = footballMatch.getHomeTeam();
        FootballTeam awayTeam = footballMatch.getAwayTeam();

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

    private Map<Position, FootballPlayer> getGoalKeeper(LineUp lineUp) {
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
}
