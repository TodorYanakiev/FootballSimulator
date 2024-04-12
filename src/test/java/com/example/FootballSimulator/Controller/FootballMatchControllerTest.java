package com.example.FootballSimulator.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.FootballSimulator.FootballMatch.FootballMatchController;
import com.example.FootballSimulator.FootballMatch.FootballMatchRepository;
import com.example.FootballSimulator.FootballMatch.FootballMatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

class FootballMatchControllerTest {

    @Mock
    private FootballMatchRepository footballMatchRepository;

    @Mock
    private FootballMatchService footballMatchService;

    @InjectMocks
    private FootballMatchController footballMatchController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testViewMatch() {
        Long matchId = 1L;
        Model model = mock(Model.class);
        String expectedViewName = "/football-match/view";

        when(footballMatchService.viewMatch(matchId, model)).thenReturn(expectedViewName);

        String actualViewName = footballMatchController.viewMatch(matchId, model);

        assertEquals(expectedViewName, actualViewName);
        verify(footballMatchService).viewMatch(matchId, model);
    }
}