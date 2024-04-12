package com.example.FootballSimulator.Standings;

import com.example.FootballSimulator.League.League;
import com.example.FootballSimulator.League.LeagueRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StandingsService {
    private StandingRepository standingRepository;

    private LeagueRepository leagueRepository;

    public StandingsService(StandingRepository standingRepository, LeagueRepository leagueRepository) {
        this.standingRepository = standingRepository;
        this.leagueRepository = leagueRepository;
    }

    public String viewStandingsForLeague(Long leagueId, Model model) {
        Optional<League> optionalLeague = leagueRepository.findById(leagueId);
        if (optionalLeague.isPresent()) {
            League league = optionalLeague.get();
            List<Standing> standings = league.getStandings();
            List<Standing> sortedStandings = standings.stream()
                    .sorted((s1, s2) -> {
                        int pointsComparison = Integer.compare(s2.getPoints(), s1.getPoints());
                        if (pointsComparison != 0) {
                            return pointsComparison;
                        }
                        return Integer.compare(s2.getScoredGoals(), s1.getScoredGoals());
                    })
                    .collect(Collectors.toList());
            model.addAttribute("standings", sortedStandings);
        } else return "redirect:/league/get";
        return "/standings/view";
    }
}
