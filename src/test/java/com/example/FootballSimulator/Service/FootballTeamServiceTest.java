package com.example.FootballSimulator.Service;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballPlayer.FootballPlayerRepository;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.FootballTeam.FootballTeamRepository;
import com.example.FootballSimulator.FootballTeam.FootballTeamService;
import com.example.FootballSimulator.League.League;
import com.example.FootballSimulator.League.LeagueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FootballTeamServiceTest {
    @Mock
    private Model model;
    @Mock
    private FootballTeamRepository footballTeamRepository;
    @Mock
    private FootballPlayerRepository footballPlayerRepository;
    @InjectMocks
    private FootballTeamService footballTeamService;
    @Mock
    private LeagueRepository leagueRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetRandomFootballPlayers() {
        List<FootballPlayer> players = new ArrayList<>();

        players.add(new FootballPlayer());
        players.add(new FootballPlayer());
        players.add(new FootballPlayer());
        players.add(new FootballPlayer());
        players.add(new FootballPlayer());

        FootballTeamService footballTeamService = new FootballTeamService();
        int count = 3;
        List<FootballPlayer> randomPlayers = footballTeamService.getRandomFootballPlayers(players, count);
        assertEquals(count, randomPlayers.size());
        for (FootballPlayer player : randomPlayers) {
            assertTrue(players.contains(player));
        }
        count = 10;
        randomPlayers = footballTeamService.getRandomFootballPlayers(players, count);
        assertEquals(players.size(), randomPlayers.size());
        for (FootballPlayer player : randomPlayers) {
            assertTrue(players.contains(player));
        }
        players.clear();
        count = 5;
        randomPlayers = footballTeamService.getRandomFootballPlayers(players, count);
        assertEquals(0, randomPlayers.size());
    }

    @Test
    void testGetRandomFootballTeam() {
        FootballTeam footballTeam1 = new FootballTeam();
        FootballTeam footballTeam2 = new FootballTeam();
        FootballTeam footballTeam3 = new FootballTeam();
        FootballTeam footballTeam4 = new FootballTeam();
        FootballTeam footballTeam5 = new FootballTeam();
        List<FootballTeam> teams = Arrays.asList(footballTeam1, footballTeam2, footballTeam3, footballTeam4, footballTeam5);
        int count = 3;
        List<FootballTeam> randomTeams = footballTeamService.getRandomFootballTeam(teams, count);
        assertEquals(81, Collections.class.getDeclaredMethods().length, "Collections.shuffle should be called once");
        assertEquals(Math.min(count, teams.size()), randomTeams.size(), "Size of randomTeams should be equal to count or the size of the input list, whichever is smaller");
        randomTeams.forEach(team -> {
            assertTrue(teams.contains(team), "Random team should be contained in the input list");
        });
    }

    @Test
    void testChooseFootballPlayersForSale() {
        FootballTeam footballTeam = new FootballTeam();
        when(footballTeamRepository.findById(anyLong())).thenReturn(Optional.of(footballTeam));
        String viewName = footballTeamService.chooseFootballPlayersForSale(1L, model);
        verify(footballTeamRepository, times(1)).findById(ArgumentMatchers.eq(1L));
        verify(model, times(1)).addAttribute(eq("footballTeam"), eq(footballTeam));
        verify(model, times(1)).addAttribute(eq("allFootballPlayersOfTheTeam"), any());
        verify(model, times(1)).addAttribute(eq("footballPlayersForSale"), any());
        assertEquals("/football-team/sale-players", viewName);
    }

    @Test
    void testGetAllFootballPlayersForSale() {
        List<FootballPlayer> mockFootballPlayers = new ArrayList<>();
        when(footballPlayerRepository.getPlayersByTeamId(anyLong())).thenReturn(mockFootballPlayers);
        String viewName = footballTeamService.getAllFootballPlayersForSale(1L, model);
        verify(footballPlayerRepository, times(1)).getPlayersByTeamId(1L);
        verify(model, times(1)).addAttribute(eq("allFootballPlayers"), eq(mockFootballPlayers));
        assertEquals("/football-team/show-players-for-sale", viewName);
    }

    @Test
    void testSaleFootballPlayers_EnoughPlayers() {
        FootballTeam mockFootballTeam = new FootballTeam();
        when(footballTeamRepository.findById(anyLong())).thenReturn(Optional.of(mockFootballTeam));
        List<FootballPlayer> mockSelectedFootballPlayers = new ArrayList<>();
        when(footballPlayerRepository.findAllByIdIn(anyList())).thenReturn(mockSelectedFootballPlayers);
        String viewName = footballTeamService.saleFootballPlayers(1L, model, Arrays.asList(1L, 2L, 3L));
        verify(footballTeamRepository, times(1)).findById(1L);
        verify(footballPlayerRepository, times(1)).findAllByIdIn(Arrays.asList(1L, 2L, 3L));
        verify(footballPlayerRepository, times(mockSelectedFootballPlayers.size())).save(any());
        verify(model, times(1)).addAttribute(eq("allFootballPlayers"), any());
        assertEquals("/football-team/show-players-for-sale", viewName);
    }

    @Test
    void testBuyFootballPlayersForHomeTeam() {
        FootballTeam mockFootballTeam = new FootballTeam();
        when(footballTeamRepository.findById(anyLong())).thenReturn(Optional.of(mockFootballTeam));
        List<FootballPlayer> mockFootballPlayersForSale = new ArrayList<>();
        when(footballPlayerRepository.findForSalePlayersExceptTeamId(anyLong())).thenReturn(mockFootballPlayersForSale);
        String viewName = footballTeamService.buyFootballPlayersForHomeTeam(1L, model);
        verify(footballTeamRepository, times(1)).findById(1L);
        verify(footballPlayerRepository, times(1)).findForSalePlayersExceptTeamId(1L);
        verify(model, times(1)).addAttribute(eq("footballTeam"), eq(mockFootballTeam));
        verify(model, times(1)).addAttribute(eq("footballPlayersForSale"), eq(mockFootballPlayersForSale));
        verify(model, times(1)).addAttribute(eq("buyFootballPlayers"), any());
        assertEquals("/football-team/buy-football-players", viewName);
    }

    @Test
    void testShowBoughtPlayers() {
        FootballTeam mockFootballTeam = new FootballTeam();
        when(footballTeamRepository.findById(anyLong())).thenReturn(Optional.of(mockFootballTeam));
        List<FootballPlayer> mockBoughtFootballPlayers = new ArrayList<>();
        when(footballPlayerRepository.getPlayersByTeamId(anyLong())).thenReturn(mockBoughtFootballPlayers);
        String viewName = footballTeamService.showBoughtPlayers(1L, model);
        verify(footballTeamRepository, times(1)).findById(1L);
        verify(footballPlayerRepository, times(1)).getPlayersByTeamId(1L);
        verify(model, times(1)).addAttribute(eq("boughtFootballPlayers"), eq(mockBoughtFootballPlayers));
        assertEquals("/football-team/show-bought-players", viewName);
    }

    @Test
    void testGetAllFootballPlayers() {
        List<FootballPlayer> mockFootballPlayers = new ArrayList<>();
        when(footballPlayerRepository.getPlayersByTeamId(anyLong())).thenReturn(mockFootballPlayers);
        String viewName = footballTeamService.getAllFootballPlayers(1L, model);
        verify(footballPlayerRepository, times(1)).getPlayersByTeamId(1L);
        verify(model, times(1)).addAttribute(eq("allFootballPlayers"), eq(mockFootballPlayers));
        assertEquals("/football-team/showFootballPlayers", viewName);
    }
    @Test
    void testViewTeam() {
        FootballTeam mockFootballTeam = new FootballTeam();
        when(footballTeamRepository.findById(anyLong())).thenReturn(Optional.of(mockFootballTeam));
        String viewName = footballTeamService.viewTeam(1L, model);
        verify(footballTeamRepository, times(1)).findById(1L);
        verify(model, times(1)).addAttribute(eq("footballTeam"), eq(mockFootballTeam));
        assertEquals("/football-team/view", viewName);
    }

    @Test
    void testViewAllTeamsByLeague() {
        League mockLeague = new League();
        mockLeague.setId(1L);
        List<FootballTeam> mockFootballTeamList = new ArrayList<>();
        FootballTeam mockFootballTeam1 = new FootballTeam();
        FootballTeam mockFootballTeam2 = new FootballTeam();
        mockFootballTeamList.add(mockFootballTeam1);
        mockFootballTeamList.add(mockFootballTeam2);
        mockLeague.setFootballTeamList(mockFootballTeamList);
        when(leagueRepository.findById(1L)).thenReturn(Optional.of(mockLeague));

        String viewName = footballTeamService.viewAllTeamsByLeague(1L, model);

        verify(leagueRepository, times(1)).findById(1L);
        verify(model, times(1)).addAttribute(eq("league"), eq(mockLeague));
        verify(model, times(1)).addAttribute(eq("footballTeams"), eq(mockFootballTeamList));

        assertEquals("/football-team/teams-for-league", viewName);
    }

    @Test
    void testViewAllAvailableTeamsByLeague() {
        League mockLeague = new League();
        mockLeague.setId(1L);
        List<FootballTeam> mockFootballTeamList = new ArrayList<>();
        FootballTeam mockFootballTeam1 = new FootballTeam();
        FootballTeam mockFootballTeam2 = new FootballTeam();
        mockFootballTeamList.add(mockFootballTeam1);
        mockFootballTeamList.add(mockFootballTeam2);
        mockLeague.setFootballTeamList(mockFootballTeamList);
        when(leagueRepository.findById(1L)).thenReturn(Optional.of(mockLeague));

        String viewName = footballTeamService.viewAllAvailableTeamsByLeague(1L, model);

        verify(leagueRepository, times(1)).findById(1L);

        verify(model, times(1)).addAttribute(eq("league"), eq(mockLeague));
        verify(model, times(1)).addAttribute(eq("footballTeams"), anyList());

        assertEquals("/football-team/available-teams-for-league", viewName);
    }

}