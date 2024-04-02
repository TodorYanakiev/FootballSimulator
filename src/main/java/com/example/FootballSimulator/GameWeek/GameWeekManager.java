package com.example.FootballSimulator.GameWeek;

import com.example.FootballSimulator.Constants.MatchStatus;
import com.example.FootballSimulator.FootballMatch.FootballMatch;
import com.example.FootballSimulator.League.League;

import java.util.ArrayList;
import java.util.List;

public class GameWeekManager {
    public static List<GameWeek> generateGameWeeks(League league) {
        int numberOfTeams = league.getFootballTeamList().size();
        List<GameWeek> gameWeekList = new ArrayList<>();
        for (int i = 0; i < (numberOfTeams - 1)*2; i++) {
            GameWeek gameWeek = new GameWeek();
            gameWeek.setWeekNumber((byte)(i + 1));
            gameWeek.setLeague(league);
            List<FootballMatch> footballMatchList = new ArrayList<>();
            for (int k = 0; k < numberOfTeams / 2; k++) {
                FootballMatch footballMatch = new FootballMatch();
                footballMatch.setGameWeek(gameWeek);
                if (i % 2 == 1) {
                    footballMatch.setHomeTeam(league.getFootballTeamList().get(k));
                    footballMatch.setAwayTeam(league.getFootballTeamList().get(numberOfTeams - k));
                }else {
                    footballMatch.setHomeTeam(league.getFootballTeamList().get(numberOfTeams - k));
                    footballMatch.setAwayTeam(league.getFootballTeamList().get(k));
                }
                footballMatch.setMatchStatus(MatchStatus.NOT_STARTED);
                footballMatch.setHomeTeamScore((byte)0);
                footballMatch.setAwayTeamScore((byte)0);
                footballMatchList.add(footballMatch);
            }
            gameWeek.setMatchList(footballMatchList);
            gameWeekList.add(gameWeek);
        }
        return gameWeekList;
    }
}
