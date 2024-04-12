package com.example.FootballSimulator.Controller;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerController;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = BaseFootballPlayerController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = BaseFootballPlayerController.class
                )
        }
)
class BaseFootballPlayerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BaseFootballPlayerRepository baseFootballPlayerRepository;

    @MockBean
    private BaseFootballPlayerService baseFootballPlayerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllPlayers() throws Exception {
        when(baseFootballPlayerService.allPlayers(any(Model.class))).thenReturn("/base-football-player/all");
        mockMvc.perform(get("/base-football-player/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("/base-football-player/all"));
    }

    @Test
    void testPlayerSubmitValid() throws Exception {
        BaseFootballPlayer player = new BaseFootballPlayer();
        player.setFirstName("John");
        player.setLastName("Doe");

        when(baseFootballPlayerService.playerSubmit(any(BaseFootballPlayer.class), any(), any(Model.class)))
                .thenReturn("redirect:/base-football-player/all");

        mockMvc.perform(post("/base-football-player/add")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/base-football-player/all"));
    }

    @Test
    void testPlayerSubmitInvalid() throws Exception {
        when(baseFootballPlayerService.playerSubmit(any(BaseFootballPlayer.class), any(), any(Model.class)))
                .thenReturn("/base-football-player/add");

        mockMvc.perform(post("/base-football-player/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("/base-football-player/add"));
    }
}