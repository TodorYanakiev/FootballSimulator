package com.example.FootballSimulator.GameWeek;

import com.example.FootballSimulator.Constants.Status;
import com.example.FootballSimulator.FootballMatch.FootballMatch;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.League.League;
import java.util.*;


public class GameWeekManager {
    public List<GameWeek> generateGameWeeks(League league) {
        int numberOfTeams = league.getFootballTeamList().size();
        List<GameWeek> gameWeekList = new ArrayList<>();
        int totalRounds = numberOfTeams - 1;
        int matchesPerRound = numberOfTeams / 2;
        for (int round = 0; round < 2 * totalRounds; round++) {
            GameWeek gameWeek = generateSingleGameWeek(league, round, numberOfTeams, matchesPerRound);
            gameWeekList.add(gameWeek);
        }
        return shuffleGameWeekList(gameWeekList);
    }

    private GameWeek generateSingleGameWeek(League league, int round, int numberOfTeams, int matchesPerRound) {
        GameWeek gameWeek = new GameWeek();
        gameWeek.setLeague(league);
        List<FootballMatch> footballMatchList = new ArrayList<>();
        for (int match = 0; match < matchesPerRound; match++) {
            FootballMatch footballMatch = generateSingleFootballMatch(gameWeek, league, round, match, numberOfTeams);
            footballMatchList.add(footballMatch);
        }
        gameWeek.setMatchList(footballMatchList);
        gameWeek.setGameWeekStatus(Status.NOT_STARTED);
        return gameWeek;
    }

    private FootballMatch generateSingleFootballMatch(GameWeek gameWeek, League league, int round, int match, int numberOfTeams) {
        List<FootballTeam> footballTeamList = league.getFootballTeamList();
        int homeIndex, awayIndex;
        if (round < numberOfTeams - 1) {
            homeIndex = (round + match) % (numberOfTeams - 1);
            awayIndex = (numberOfTeams - 1 - match + round) % (numberOfTeams - 1);
            if (match == 0) {
                awayIndex = numberOfTeams - 1;
            }
        } else {
            homeIndex = (round + match) % (numberOfTeams - 1);
            awayIndex = (numberOfTeams - 1 - match + round) % (numberOfTeams - 1);
            if (match == 0) {
                awayIndex = numberOfTeams - 1;
            }
            int temp = homeIndex;
            homeIndex = awayIndex;
            awayIndex = temp;
        }
        FootballMatch footballMatch = new FootballMatch();
        footballMatch.setGameWeek(gameWeek);
        footballMatch.setHomeTeam(footballTeamList.get(homeIndex));
        footballMatch.setAwayTeam(footballTeamList.get(awayIndex));
        footballMatch.setMatchStatus(Status.NOT_STARTED);
        footballMatch.setHomeTeamScore((byte) 0);
        footballMatch.setAwayTeamScore((byte) 0);
        footballMatch.setAwayAttacks((byte)0);
        footballMatch.setAwayShots((byte)0);
        footballMatch.setHomeShots((byte)0);
        footballMatch.setHomeAttacks((byte)0);
        footballMatch.setDangerAwayAttacks((byte)0);
        footballMatch.setDangerHomeAttacks((byte)0);
        return footballMatch;
    }

    public static List<GameWeek> shuffleGameWeekList(List<GameWeek> gameWeekList) {
        int size = gameWeekList.size();
        List<GameWeek> shuffledGameWeekList = Arrays.asList(new GameWeek[size]);
        List<GameWeek> firstHalf = gameWeekList.subList(0, size / 2);
        List<GameWeek> secondHalf = gameWeekList.subList(size / 2, size);
        for (int i = 0; i < size / 2; i++) {
            if (i % 2 == 0) {
                shuffledGameWeekList.set(i, secondHalf.get(i));
                shuffledGameWeekList.set(size / 2 + i, firstHalf.get(i));
            } else {
                shuffledGameWeekList.set(i, firstHalf.get(i));
                shuffledGameWeekList.set(size / 2 + i, secondHalf.get(i));
            }
        }
        for (int i = 0; i < size; i++) {
            shuffledGameWeekList.get(i).setWeekNumber((byte) (i + 1));
        }
        return shuffledGameWeekList;
    }


}
