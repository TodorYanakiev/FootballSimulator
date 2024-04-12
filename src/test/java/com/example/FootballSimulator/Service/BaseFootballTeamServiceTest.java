package com.example.FootballSimulator.Service;

import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeamRepository;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeamService;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

class BaseFootballTeamServiceTest {
    @InjectMocks
    private BaseFootballTeamService baseFootballTeamService;

    @Mock
    private BaseFootballTeamRepository baseFootballTeamRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAllTeams() {
        Model model = mock(Model.class);
        String viewName = baseFootballTeamService.allTeams(model);
        assertEquals("/base-football-team/all", viewName);
        verify(model).addAttribute("baseTeams", baseFootballTeamRepository.findAll());
    }

    @Test
    void testTeamForm() {
        Model model = mock(Model.class);
        String viewName = baseFootballTeamService.teamForm(model);
        assertEquals("/base-football-team/add", viewName);
    }

    @Test
    void testTeamSubmitWithNoErrors() {
        BaseFootballTeam baseFootballTeam = new BaseFootballTeam();
        BindingResult bindingResult = mock(BindingResult.class);
        String viewName = baseFootballTeamService.teamSubmit(baseFootballTeam, bindingResult);
        assertEquals("redirect:/base-football-team/all", viewName);
        verify(bindingResult).hasErrors();
        verify(baseFootballTeamRepository).save(baseFootballTeam);
    }

    @Test
    void testTeamSubmitWithErrors() {
        BaseFootballTeam baseFootballTeam = new BaseFootballTeam();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = baseFootballTeamService.teamSubmit(baseFootballTeam, bindingResult);
        assertEquals("/base-football-team/add", viewName);
        verify(bindingResult).hasErrors();
    }
}