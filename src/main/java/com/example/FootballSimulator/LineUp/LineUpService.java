package com.example.FootballSimulator.LineUp;

import com.example.FootballSimulator.Constants.Position;
import com.example.FootballSimulator.Constants.TeamFormation;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LineUpService {
    private LineUpRepository lineUpRepository;

    public Map<Position, FootballPlayer> getPositionFootballPlayerByFormation(TeamFormation teamFormation) {
        Map<Position, FootballPlayer> positionFootballPlayerMap = new HashMap<>();
        if (teamFormation.equals(TeamFormation.FOUR_THREE_THREE)) {
            positionFootballPlayerMap.put(Position.GK, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.CM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.CF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LF, new FootballPlayer());
        } else if (teamFormation.equals(TeamFormation.FOUR_FOUR_TWO)) {
            positionFootballPlayerMap.put(Position.GK, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCF, new FootballPlayer());
        }
        return positionFootballPlayerMap;
    }
}
