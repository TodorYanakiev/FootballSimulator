package com.example.FootballSimulator.FootballTeam;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.FootballPlayer.FootballPlayerRepository;
import com.example.FootballSimulator.League.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
        return footballTeamService.viewAllTeamsByLeague(leagueId, model);
    }

    @GetMapping("/all-available/{leagueId}")
    public String viewAllAvailableTeamsByLeague(@PathVariable("leagueId") Long leagueId, Model model) {
        return footballTeamService.viewAllAvailableTeamsByLeague(leagueId, model);
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
    public String getAllFootballPlayers(@RequestParam("teamId") Long teamId,Model model) {
       return footballTeamService.getAllFootballPlayers(teamId,model);
    }
    @GetMapping("/saleFootballPlayers/{teamId}")
    public String saleFootballPlayers(@PathVariable("teamId") Long teamId, Model model) {
        return footballTeamService.chooseFootballPlayersForSale(teamId,model);

    }
    @GetMapping("/getFootballPlayersForSale/{teamId}")
    public String getAllFootballPlayersForSale(@PathVariable("teamId") Long teamId,Model model) {
        return footballTeamService.getAllFootballPlayersForSale(teamId,model);
    }

    @PostMapping("/sale")
    public String saleFootballPlayers(@RequestParam("teamId") Long teamId,Model model, @RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds){
        return footballTeamService.saleFootballPlayers(teamId,model,selectedFootballPlayerIds);
    }
    @PostMapping("/submit")
    public String addFootballPlayersToTeam(@RequestParam("teamId") Long teamId,Model model, @RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds) {
        return footballTeamService.addFootballPlayersToTeam(teamId,model,selectedFootballPlayerIds);
    }
    @GetMapping("/buy-players-for-home-team/{teamId}")
    public String buyFootballPlayersForHomeTeam(@PathVariable("teamId") Long teamId,Model model){
       return footballTeamService.buyFootballPlayersForHomeTeam(teamId,model);
    }
    @GetMapping("/show-bought-players/{teamId}")
    public String showBoughtPlayers(@PathVariable("teamId") Long teamId,Model model){
        return footballTeamService.showBoughtPlayers(teamId,model);
    }
    @PostMapping("/buy")
    public String buyFootballPlayer(@RequestParam("teamId") Long teamId,Model model,@RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds){
       return footballTeamService.buyFootballPlayer(teamId,model,selectedFootballPlayerIds);
    }

    @GetMapping("/select-team/{teamId}")
    public String selectTeam(@PathVariable("teamId") Long teamId, Model model) {
        return footballTeamService.selectTeam(teamId, model);
    }

    @GetMapping("/view/{teamId}")
    public String viewTeam(@PathVariable("teamId") Long teamId, Model model) {
        return footballTeamService.viewTeam(teamId, model);
    }
}
