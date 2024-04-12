package com.example.FootballSimulator.LineUp;

import com.example.FootballSimulator.Constants.Position;
import com.example.FootballSimulator.Constants.TeamFormation;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballTeam.FootballTeam;
import com.example.FootballSimulator.FootballTeam.FootballTeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LineUpService {
    private LineUpRepository lineUpRepository;

    private FootballTeamRepository footballTeamRepository;

    public LineUpService(LineUpRepository lineUpRepository, FootballTeamRepository footballTeamRepository) {
        this.lineUpRepository = lineUpRepository;
        this.footballTeamRepository = footballTeamRepository;
    }

    public String addLineUpFormation(Long teamId, Model model) {
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

    public String addPlayersToLineUp(LineUp lineUp, Model model) {
        FootballTeam footballTeam = lineUp.getFootballTeam();
        TeamFormation teamFormation = lineUp.getFootballFormation();
        lineUp.setPositionFootballPlayerMap(getPositionFootballPlayerByFormation(teamFormation));
        model.addAttribute("allPlayers", footballTeam.getPlayerList());
        if (teamFormation.equals(TeamFormation.FOUR_THREE_THREE)) {
            return "/line-up/team-formation/4-3-3";
        } else if(teamFormation.equals(TeamFormation.FOUR_FOUR_TWO)){
            return "/line-up/team-formation/4-4-2";
        } else if(teamFormation.equals(TeamFormation.FOUR_TWO_FOUR)){
            return "/line-up/team-formation/4-2-4";
        } else {
            return "/line-up/team-formation/3-3-4";
        }
    }

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

    public Map<Position, FootballPlayer> getPositionFootballPlayerByFormation(TeamFormation teamFormation) {
        Map<Position, FootballPlayer> positionFootballPlayerMap = new HashMap<>();
        if (teamFormation.equals(TeamFormation.FOUR_THREE_THREE)) {
            positionFootballPlayerMap.put(Position.GK, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.CM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.CF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LF, new FootballPlayer());
        } else if (teamFormation.equals(TeamFormation.FOUR_FOUR_TWO)) {
            positionFootballPlayerMap.put(Position.GK, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCF, new FootballPlayer());
        } else if(teamFormation.equals(TeamFormation.FOUR_TWO_FOUR)){
            positionFootballPlayerMap.put(Position.GK, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LF, new FootballPlayer());
        } else if(teamFormation.equals(TeamFormation.THREE_THREE_FOUR)) {
            positionFootballPlayerMap.put(Position.GK, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.CB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCB, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.CM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCM, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.RCF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LCF, new FootballPlayer());
            positionFootballPlayerMap.put(Position.LF, new FootballPlayer());
        }
        return positionFootballPlayerMap;
    }

    public String deleteLineUp(Long lineUpId, Model model) {
        Optional<LineUp> optionalLineUp = lineUpRepository.findById(lineUpId);
        if (optionalLineUp.isPresent()) {
            LineUp lineUp = optionalLineUp.get();
            Long id = lineUp.getFootballTeam().getId();
            lineUpRepository.delete(lineUp);
            model.addAttribute("message", "Line-up deleted.");
            model.addAttribute("footballTeam", lineUp.getFootballTeam());
            return "/football-team/view";
            //return "redirect:/line-up/add/" + optionalFootballTeam.get().getId();
        } else {
            model.addAttribute("message", "There is no line-up to delete.");
            return "/home";
        }
    }

    public String viewLineUp(Long teamId, Model model) {
        Optional<FootballTeam> optionalFootballTeam = footballTeamRepository.findById(teamId);
        if (optionalFootballTeam.isPresent()) {
            LineUp lineUp = optionalFootballTeam.get().getLineUp();
            model.addAttribute("lineUp", lineUp);
            return "/line-up/view";
        }
        return "/home";
    }
}
