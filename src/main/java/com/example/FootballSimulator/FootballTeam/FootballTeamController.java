package com.example.FootballSimulator.FootballTeam;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballPlayer.FootballPlayerRepository;
import com.example.FootballSimulator.League.League;
import com.example.FootballSimulator.TransferSumCalculator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/football-team")
public class FootballTeamController {
   @Autowired
   private FootballTeamService footballTeamService;
    @GetMapping("/add/{teamId}")
    public String addPlayersToTeam(@PathVariable("teamId") Long teamId, Model model) {
        return footballTeamService.addPlayersToTeam(teamId,model);
    }
    @GetMapping("/getFootballPlayers")
    public String getAllFootballPlayers(Model model) {
       return footballTeamService.getAllFootballPlayers(model);
    }
    @PostMapping("/submit")
    public String addFootballPlayersToTeam(@RequestParam("teamId") Long teamId,Model model, @RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds) {
       return footballTeamService.addFootballPlayersToTeam(teamId,model,selectedFootballPlayerIds);
    }
    @GetMapping("/saleFootballPlayers/{teamId}")
    public String saleFootballPlayers(@PathVariable("teamId") Long teamId, Model model) {
       return footballTeamService.chooseFootballPlayersForSale(teamId,model);

    }
    @GetMapping("/getFootballPlayersForSale")
    public String getAllFootballPlayersForSale(Model model) {
       return footballTeamService.getAllFootballPlayersForSale(model);
    }

    @PostMapping("/sale")
    public String saleFootballPlayers(@RequestParam("teamId") Long teamId,Model model, @RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds){
        return footballTeamService.saleFootballPlayers(teamId,model,selectedFootballPlayerIds);
    }
}
