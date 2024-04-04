package com.example.FootballSimulator.FootballTeam;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.BaseFootballTeam.BaseFootballTeam;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballPlayer.FootballPlayerRepository;
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
    @GetMapping("/add/{teamId}")
    public String addPlayersToTeam(@PathVariable("teamId") Long teamId, Model model) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        model.addAttribute("footballTeam", footballTeam);
        model.addAttribute("allBaseFootballPlayers",baseFootballPlayerRepository.findAll());
        model.addAttribute("baseFootballPlayers",new ArrayList<BaseFootballPlayer>());
        return "/football-team/add-players";
    }
    @GetMapping("/getFootballPlayers")
    public String getAllFootballPlayers(Model model) {
       model.addAttribute("allFootballPlayers",footballPlayerRepository.findAll());
       return "/football-team/showFootballPlayers";
    }
    @PostMapping("/submit")
    public String addFootballPlayersToTeam(@RequestParam("teamId") Long teamId,Model model, @RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds) {
        Optional<FootballTeam> footballTeamOptional = footballTeamRepository.findById(teamId);
        FootballTeam footballTeam = new FootballTeam();
        if (footballTeamOptional.isPresent()){
            footballTeam = footballTeamOptional.get();
        }
        List<BaseFootballPlayer> selectedFootballPlayers = baseFootballPlayerRepository.findAllByIdIn(selectedFootballPlayerIds);
        for (BaseFootballPlayer selectedFootballPlayer : selectedFootballPlayers) {
            FootballPlayer footballPlayer = new FootballPlayer();
            footballPlayer.setFootballTeam(footballTeam);
            footballPlayer.setBaseFootballPlayer(selectedFootballPlayer);
            footballPlayer.setDefending(selectedFootballPlayer.getStartDefending());
            footballPlayer.setDribble(selectedFootballPlayer.getStartDribble());
            footballPlayer.setGoalkeeping(selectedFootballPlayer.getStartGoalkeeping());
            footballPlayer.setPassing(selectedFootballPlayer.getStartPassing());
            footballPlayer.setPositioning(selectedFootballPlayer.getStartPositioning());
            footballPlayer.setScoring(selectedFootballPlayer.getStartScoring());
            footballPlayer.setSpeed(selectedFootballPlayer.getStartSpeed());
            footballPlayer.setStamina(selectedFootballPlayer.getStartStamina());
            footballPlayer.setPrice(1);
            footballPlayerRepository.save(footballPlayer);
        }
        return "redirect:/football-team/getFootballPlayers";
    }
}
