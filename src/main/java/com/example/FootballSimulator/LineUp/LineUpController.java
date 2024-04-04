package com.example.FootballSimulator.LineUp;

import com.example.FootballSimulator.Constants.TeamFormation;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.FootballTeam.FootballTeamRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/line-up")
public class LineUpController {
    private LineUpService lineUpService;

    private LineUpRepository lineUpRepository;

    private FootballTeamRepository footballTeamRepository;

    public LineUpController(LineUpService lineUpService,LineUpRepository lineUpRepository , FootballTeamRepository footballTeamRepository) {
        this.lineUpService = lineUpService;
        this.lineUpRepository = lineUpRepository;
        this.footballTeamRepository = footballTeamRepository;
    }

    @GetMapping("/add/{teamId}")
    public String addLineUpFormation(@PathVariable("teamId") Long teamId, Model model) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
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
            return "/line-up/team-formation/433";
        } else {
            return "/line-up/team-formation/442";
        }
    }

    @PostMapping("/add")
    public String submitLineUp(LineUp lineUp, Model model) {
        lineUpRepository.save(lineUp);
        return "redirect:/line-up/select";
    }
}
