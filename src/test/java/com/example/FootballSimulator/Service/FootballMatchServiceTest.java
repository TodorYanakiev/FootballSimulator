package com.example.FootballSimulator.Service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.FootballSimulator.Constants.Position;
import com.example.FootballSimulator.Constants.Status;
import com.example.FootballSimulator.FootballMatch.FootballMatch;
import com.example.FootballSimulator.FootballMatch.FootballMatchRepository;
import com.example.FootballSimulator.FootballMatch.FootballMatchService;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;



class FootballMatchServiceTest {

    @Mock
    private FootballMatchRepository footballMatchRepository;

    @Mock
    private StandingRepository standingsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FootballTeamRepository footballTeamRepository;
    @Mock
    private LineUp lineUp;

    @Mock
    private GameWeekRepository gameWeekRepository;
    @Mock
    private TransferSumCalculator transferSumCalculator;

    @InjectMocks
    private FootballMatchService footballMatchService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testViewMatchWithExistingMatch() {
        Long matchId = 1L;
        FootballMatch footballMatch = new FootballMatch();
        Model model = mock(Model.class);
        when(footballMatchRepository.findById(matchId)).thenReturn(Optional.of(footballMatch));
        String viewName = footballMatchService.viewMatch(matchId, model);
        assertEquals("/football-match/view", viewName);
    }

    @Test
    void testViewMatchWithNonExistingMatch() {
        Long matchId = 1L;
        Model model = mock(Model.class);
        when(footballMatchRepository.findById(matchId)).thenReturn(Optional.empty());
        String viewName = footballMatchService.viewMatch(matchId, model);
        assertEquals("/home", viewName);
    }

    @Test
    void testSimulateUserMatchWhenMatchNotFound() {
        Long matchId = 1L;
        byte matchPart = 0;
        Model model = mock(Model.class);
        when(footballMatchRepository.findById(matchId)).thenReturn(Optional.empty());
        String result = footballMatchService.simulateUserMatch(matchId, matchPart, model);
        assertEquals("/home", result);
        verify(model).addAttribute("message", "Error finding the match");
    }

    @Test
    void testSimulateUserMatchWhenMatchAlreadyFinished() {
        Long matchId = 1L;
        byte matchPart = 0;
        Model model = mock(Model.class);
        FootballMatch footballMatch = new FootballMatch();
        footballMatch.setMatchStatus(Status.FINISHED);
        when(footballMatchRepository.findById(matchId)).thenReturn(Optional.of(footballMatch));
        String result = footballMatchService.simulateUserMatch(matchId, matchPart, model);
        assertEquals("/home", result);
        verify(model).addAttribute("message", "Error, the match is finished");
    }

    @Test
    void testSimulateUserMatchWhenInvalidMatchPart() {
        Long matchId = 1L;
        byte matchPart = 10;
        Model model = mock(Model.class);
        FootballMatch footballMatch = new FootballMatch();
        footballMatch.setMatchStatus(Status.STARTED);
        when(footballMatchRepository.findById(matchId)).thenReturn(Optional.of(footballMatch));
        String result = footballMatchService.simulateUserMatch(matchId, matchPart, model);
        assertEquals("/home", result);
        verify(model).addAttribute("message", "Error, invalid match part.");
    }

    @Test
    void testSaveMatchResults() {
        FootballMatch footballMatch = new FootballMatch();
        FootballTeam homeTeam = new FootballTeam();
        homeTeam.setStanding(new Standing());
        footballMatch.setHomeTeam(homeTeam);
        FootballTeam awayTeam = new FootballTeam();
        awayTeam.setStanding(new Standing());
        footballMatch.setAwayTeam(awayTeam);

        footballMatchService.saveMatchResults(footballMatch);

        verify(footballMatchRepository).save(footballMatch);
        verify(standingsRepository).save(homeTeam.getStanding());
        verify(standingsRepository).save(awayTeam.getStanding());
    }
    /*@Test
    void testGetStartPlayerPower() {
        FootballPlayer player = new FootballPlayer();
        Position position = Position.CF;
        double expectedPower = 85.0;
        when(transferSumCalculator.calculatePlayerOverall(player, position)).thenReturn((byte) expectedPower);
        double actualPower = footballMatchService.getStartPlayerPower(player, position);
        assertEquals(expectedPower, actualPower);
        verify(transferSumCalculator).calculatePlayerOverall(player, position);
    }*/
/*

    @Test
    void testGetGoalkeeper() {
        // Arrange
        FootballPlayer goalkeeper = new FootballPlayer();
        goalkeeper.pos(Position.GK);

        // Create a map with various positions
        Map<Position, FootballPlayer> positionFootballPlayerMap = new HashMap<>();
        positionFootballPlayerMap.put(Position.GK, goalkeeper);
        positionFootballPlayerMap.put(Position.LB, new FootballPlayer());
        positionFootballPlayerMap.put(Position.CB, new FootballPlayer());

        // Mock the behavior of the LineUp object
        when(lineUp.getPositionFootballPlayerMap()).thenReturn(positionFootballPlayerMap);

        // Act
        Map<Position, FootballPlayer> result = footballMatchService.getGoalkeeper(lineUp);

        // Assert
        assertEquals(1, result.size()); // Check that only one player is in the map
        assertEquals(goalkeeper, result.get(Position.GK)); // Check that the goalkeeper is in the map
        // Verify that getPositionFootballPlayerMap() was called
        verify(lineUp).getPositionFootballPlayerMap();
    }
*/

    @Test
    void testGetGoalkeeper_NoGoalkeeper() {
        Map<Position, FootballPlayer> positionFootballPlayerMap = new HashMap<>();
        positionFootballPlayerMap.put(Position.LB, new FootballPlayer());
        positionFootballPlayerMap.put(Position.CB, new FootballPlayer());

        when(lineUp.getPositionFootballPlayerMap()).thenReturn(positionFootballPlayerMap);
        Map<Position, FootballPlayer> result = footballMatchService.getGoalkeeper(lineUp);
        assertEquals(0, result.size());
        verify(lineUp).getPositionFootballPlayerMap();
    }
    @Test
    void testGetDefenseLine() {
        FootballPlayer cbPlayer = new FootballPlayer();
        FootballPlayer lcbPlayer = new FootballPlayer();
        FootballPlayer rcbPlayer = new FootballPlayer();
        FootballPlayer lbPlayer = new FootballPlayer();
        FootballPlayer rbPlayer = new FootballPlayer();

        Map<Position, FootballPlayer> positionFootballPlayerMap = new HashMap<>();
        positionFootballPlayerMap.put(Position.CB, cbPlayer);
        positionFootballPlayerMap.put(Position.LCB, lcbPlayer);
        positionFootballPlayerMap.put(Position.RCB, rcbPlayer);
        positionFootballPlayerMap.put(Position.LB, lbPlayer);
        positionFootballPlayerMap.put(Position.RB, rbPlayer);

        when(lineUp.getPositionFootballPlayerMap()).thenReturn(positionFootballPlayerMap);
        Map<Position, FootballPlayer> result = footballMatchService.getDefenseLine(lineUp);
        assertEquals(5, result.size());
        assertEquals(cbPlayer, result.get(Position.CB));
        assertEquals(lcbPlayer, result.get(Position.LCB));
        assertEquals(rcbPlayer, result.get(Position.RCB));
        assertEquals(lbPlayer, result.get(Position.LB));
        assertEquals(rbPlayer, result.get(Position.RB));
    }

    @Test
    void testGetDefenseLine_NoDefensePlayers() {
        Map<Position, FootballPlayer> positionFootballPlayerMap = new HashMap<>();
        positionFootballPlayerMap.put(Position.CM, new FootballPlayer());
        positionFootballPlayerMap.put(Position.LM, new FootballPlayer());
        positionFootballPlayerMap.put(Position.RM, new FootballPlayer());
        when(lineUp.getPositionFootballPlayerMap()).thenReturn(positionFootballPlayerMap);
        Map<Position, FootballPlayer> result = footballMatchService.getDefenseLine(lineUp);
        assertEquals(0, result.size());
    }

    @Test
    void testGetAttackLine() {
        FootballPlayer cfPlayer = new FootballPlayer();
        FootballPlayer lcfPlayer = new FootballPlayer();
        FootballPlayer rcfPlayer = new FootballPlayer();
        FootballPlayer lfPlayer = new FootballPlayer();
        FootballPlayer rfPlayer = new FootballPlayer();

        Map<Position, FootballPlayer> positionFootballPlayerMap = new HashMap<>();
        positionFootballPlayerMap.put(Position.CF, cfPlayer);
        positionFootballPlayerMap.put(Position.LCF, lcfPlayer);
        positionFootballPlayerMap.put(Position.RCF, rcfPlayer);
        positionFootballPlayerMap.put(Position.LF, lfPlayer);
        positionFootballPlayerMap.put(Position.RF, rfPlayer);

        when(lineUp.getPositionFootballPlayerMap()).thenReturn(positionFootballPlayerMap);
        Map<Position, FootballPlayer> result = footballMatchService.getAttackLine(lineUp);

        assertEquals(5, result.size());
        assertEquals(cfPlayer, result.get(Position.CF));
        assertEquals(lcfPlayer, result.get(Position.LCF));
        assertEquals(rcfPlayer, result.get(Position.RCF));
        assertEquals(lfPlayer, result.get(Position.LF));
        assertEquals(rfPlayer, result.get(Position.RF));
    }

    @Test
    void testGetAttackLine_NoAttackPlayers() {
        Map<Position, FootballPlayer> positionFootballPlayerMap = new HashMap<>();
        positionFootballPlayerMap.put(Position.CM, new FootballPlayer());
        positionFootballPlayerMap.put(Position.LM, new FootballPlayer());
        positionFootballPlayerMap.put(Position.RM, new FootballPlayer());

        when(lineUp.getPositionFootballPlayerMap()).thenReturn(positionFootballPlayerMap);

        Map<Position, FootballPlayer> result = footballMatchService.getAttackLine(lineUp);

        assertEquals(0, result.size());
    }

    @Test
    void testGetMidfieldLine() {
        FootballPlayer cmPlayer = new FootballPlayer();
        FootballPlayer lcmPlayer = new FootballPlayer();
        FootballPlayer rcmPlayer = new FootballPlayer();
        FootballPlayer lmPlayer = new FootballPlayer();
        FootballPlayer rmPlayer = new FootballPlayer();
        Map<Position, FootballPlayer> positionFootballPlayerMap = new HashMap<>();
        positionFootballPlayerMap.put(Position.CM, cmPlayer);
        positionFootballPlayerMap.put(Position.LCM, lcmPlayer);
        positionFootballPlayerMap.put(Position.RCM, rcmPlayer);
        positionFootballPlayerMap.put(Position.LM, lmPlayer);
        positionFootballPlayerMap.put(Position.RM, rmPlayer);
        when(lineUp.getPositionFootballPlayerMap()).thenReturn(positionFootballPlayerMap);
        Map<Position, FootballPlayer> result = footballMatchService.getMidfieldLine(lineUp);
        assertEquals(5, result.size());
        assertEquals(cmPlayer, result.get(Position.CM));
        assertEquals(lcmPlayer, result.get(Position.LCM));
        assertEquals(rcmPlayer, result.get(Position.RCM));
        assertEquals(lmPlayer, result.get(Position.LM));
        assertEquals(rmPlayer, result.get(Position.RM));
    }

    @Test
    void testGetMidfieldLine_NoMidfieldPlayers() {
        Map<Position, FootballPlayer> positionFootballPlayerMap = new HashMap<>();
        positionFootballPlayerMap.put(Position.CF, new FootballPlayer());
        positionFootballPlayerMap.put(Position.LF, new FootballPlayer());
        positionFootballPlayerMap.put(Position.RF, new FootballPlayer());
        when(lineUp.getPositionFootballPlayerMap()).thenReturn(positionFootballPlayerMap);
        Map<Position, FootballPlayer> result = footballMatchService.getMidfieldLine(lineUp);
        assertEquals(0, result.size());
    }
}