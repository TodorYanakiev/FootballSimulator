package com.example.FootballSimulator.FootballTeam;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballPlayer.FootballPlayerRepository;
import com.example.FootballSimulator.League.League;
import com.example.FootballSimulator.League.LeagueRepository;
import com.example.FootballSimulator.TransferSumCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/football-team")
public class FootballTeamController {
    @Autowired
    private FootballPlayerRepository footballPlayerRepository;

    @Autowired
    private BaseFootballPlayerRepository baseFootballPlayerRepository;
    @Autowired
    private FootballTeamRepository footballTeamRepository;

    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private FootballTeamService footballTeamService;

    @GetMapping("/all/{leagueId}")
    public String viewAllTeamsByLeague(@PathVariable("leagueId") Long leagueId, Model model) {
        League league = leagueRepository.findById(leagueId).orElseThrow(() -> new IllegalArgumentException("Invalid league ID"));
        List<FootballTeam> footballTeamList = league.getFootballTeamList();
        model.addAttribute("league", league);
        model.addAttribute("footballTeams", footballTeamList);
        return "/football-team/teams-for-league";
    }

    @GetMapping("/players/{teamId}")
    public String viewPlayersForTeam(@PathVariable("teamId") Long teamId, Model model) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        model.addAttribute("allPlayers", footballTeam.getPlayerList());
        model.addAttribute("footballTeam", footballTeam);
        return "/football-team/all-players";
    }

    @GetMapping("/add/{teamId}")
    public String addPlayersToTeam(@PathVariable("teamId") Long teamId, Model model) {
       return footballTeamService.addPlayersToTeam(teamId,model);
    }
    @GetMapping("/getFootballPlayers")
    public String getAllFootballPlayers(Model model) {
       return footballTeamService.getAllFootballPlayers(model);
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
    @PostMapping("/submit")
    public String addFootballPlayersToTeam(@RequestParam("teamId") Long teamId,Model model, @RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds) {
        return footballTeamService.addFootballPlayersToTeam(teamId,model,selectedFootballPlayerIds);
    }
}
