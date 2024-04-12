package com.example.FootballSimulator.Service;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerService;
import com.example.FootballSimulator.Constants.Position;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BaseFootballPlayerServiceTest {
    @Mock
    private BaseFootballPlayerRepository baseFootballPlayerRepository;

    @Mock
    private Model model;
    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private BaseFootballPlayerService baseFootballPlayerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllPlayersAddsCorrectAttributes() {
        BaseFootballPlayer baseFootballPlayer1 = new BaseFootballPlayer();
        baseFootballPlayer1.setFirstName("Player1");

        BaseFootballPlayer baseFootballPlayer2 = new BaseFootballPlayer();
        baseFootballPlayer2.setFirstName("Player2");
        List<BaseFootballPlayer> expectedPlayers = Arrays.asList(baseFootballPlayer1, baseFootballPlayer1);
        when(baseFootballPlayerRepository.findAll()).thenReturn(expectedPlayers);

        String viewName = baseFootballPlayerService.allPlayers(model);

        verify(model).addAttribute("basePlayers", expectedPlayers);
        assertEquals("/base-football-player/all", viewName);
    }

    @Test
    void testPlayerFormAddsCorrectAttributes() {
        Position[] expectedPositions = Position.values();
        String viewName = baseFootballPlayerService.playerForm(model);
        verify(model).addAttribute(eq("baseFootballPlayer"), any(BaseFootballPlayer.class));
        verify(model).addAttribute("positions", expectedPositions);
        assertEquals("/base-football-player/add", viewName);
    }

    @Test
    void testPlayerSubmitWithValidationErrors() {
        BaseFootballPlayer player = new BaseFootballPlayer();
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = baseFootballPlayerService.playerSubmit(player, bindingResult, model);
        verify(model).addAttribute("positions", Position.values());
        assertEquals("/base-football-player/add", viewName);
    }

    @Test
    void testPlayerSubmitWithNoValidationErrors() {
        BaseFootballPlayer player = new BaseFootballPlayer();
        when(bindingResult.hasErrors()).thenReturn(false);
        String viewName = baseFootballPlayerService.playerSubmit(player, bindingResult, model);
        verify(baseFootballPlayerRepository).save(player);
        assertEquals("redirect:/base-football-player/all", viewName);
    }
}