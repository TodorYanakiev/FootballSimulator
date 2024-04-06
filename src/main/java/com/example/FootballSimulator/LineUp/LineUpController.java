package com.example.FootballSimulator.LineUp;

import com.example.FootballSimulator.Constants.Position;
import com.example.FootballSimulator.Constants.TeamFormation;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.FootballTeam.FootballTeamRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/line-up")

public class LineUpController {
    private LineUpService lineUpService;

    private LineUpRepository lineUpRepository;

    private FootballTeamRepository footballTeamRepository;

    public LineUpController(LineUpService lineUpService, LineUpRepository lineUpRepository, FootballTeamRepository footballTeamRepository) {
        this.lineUpService = lineUpService;
        this.lineUpRepository = lineUpRepository;
        this.footballTeamRepository = footballTeamRepository;
    }

    @GetMapping("/add/{teamId}")
    public String addLineUpFormation(@PathVariable("teamId") Long teamId, Model model) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        if (footballTeam.getLineUp() != null) {
            model.addAttribute("lineUp", footballTeam.getLineUp());
            return "/line-up/view";
        }
        LineUp lineUp = new LineUp();
        lineUp.setFootballTeam(footballTeam);
        model.addAttribute("lineUp", lineUp);
        model.addAttribute("teamFormations", TeamFormation.values());
        return "/line-up/select-formation";
    }

    @GetMapping("/add-players")
    public String addPlayersToLineUp(LineUp lineUp, Model model) {
        FootballTeam footballTeam = lineUp.getFootballTeam();
        TeamFormation teamFormation = lineUp.getFootballFormation();
        lineUp.setPositionFootballPlayerMap(lineUpService.getPositionFootballPlayerByFormation(teamFormation));
        model.addAttribute("allPlayers", footballTeam.getPlayerList());
        if (teamFormation.equals(TeamFormation.FOUR_THREE_THREE)) {
            return "/line-up/team-formation/4-3-3";
        } else {
            return "/line-up/team-formation/4-4-2";
        }
    }

    @PostMapping("/submit")
    public String submitLineUp(LineUp lineUp, Model model) {
        try {
            for (FootballPlayer footballPlayer : lineUp.getPositionFootballPlayerMap().values()) {
                if (footballPlayer == null) {
                    model.addAttribute("message", "Every position must have different player!");
                    model.addAttribute("lineUp", lineUp);
                    model.addAttribute("allPlayers", lineUp.getFootballTeam().getPlayerList());
                    return "/line-up/team-formation/" + lineUp.getFootballFormation().getLabel();
                }
            }
            lineUpRepository.save(lineUp);
            FootballTeam footballTeam = lineUp.getFootballTeam();
            footballTeam.setLineUp(lineUp);
            footballTeamRepository.save(footballTeam);
            model.addAttribute(lineUp);
            return "/line-up/view";
        } catch (Exception e) {
            model.addAttribute("message", "Every position must have different player!");
            model.addAttribute("lineUp", lineUp);
            model.addAttribute("allPlayers", lineUp.getFootballTeam().getPlayerList());
            return "/line-up/team-formation/" + lineUp.getFootballFormation().getLabel();
        }
    }
}
