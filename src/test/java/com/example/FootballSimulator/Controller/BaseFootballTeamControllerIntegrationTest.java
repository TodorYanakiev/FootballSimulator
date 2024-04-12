package com.example.FootballSimulator.Controller;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerController;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeamController;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeamRepository;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = BaseFootballTeamController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = BaseFootballTeamController.class
                )
        }
)
class BaseFootballTeamControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BaseFootballTeamRepository baseFootballTeamRepository;
    @MockBean
    private BaseFootballTeamService baseFootballTeamService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllTeams() throws Exception {
        when(baseFootballTeamService.allTeams(any(Model.class))).thenReturn("/base-football-team/all");

        mockMvc.perform(get("/base-football-team/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("/base-football-team/all"));
    }

    @Test
    void testSubmitTeam() throws Exception {
        BaseFootballTeam baseFootballTeam = new BaseFootballTeam();
        baseFootballTeam.setBaseTeamName("baseTeamName");
        baseFootballTeam.setStadiumName("stadiumName");
        when(baseFootballTeamService.teamSubmit(eq(baseFootballTeam), any(BindingResult.class)))
                .thenReturn("/base-football-team/submitted");

        mockMvc.perform(post("/base-football-team/add")
                        .param("baseTeamName", "baseTeamName")
                        .param("stadiumName", "stadiumName"))
                .andExpect(status().isOk())
                .andExpect(view().name("base-football-team/add"));
    }
}