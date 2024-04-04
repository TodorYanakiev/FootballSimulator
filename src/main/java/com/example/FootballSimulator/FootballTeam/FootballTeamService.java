package com.example.FootballSimulator.FootballTeam;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballPlayer.FootballPlayerRepository;
import com.example.FootballSimulator.League.League;
import com.example.FootballSimulator.TransferSumCalculator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class FootballTeamService {
    @Autowired
    private FootballPlayerRepository footballPlayerRepository;

    @Autowired
    private BaseFootballPlayerRepository baseFootballPlayerRepository;
    @Autowired
    private FootballTeamRepository footballTeamRepository;
    private final EntityManager entityManager;

    public FootballTeamService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public String addPlayersToTeam(@PathVariable("teamId") Long teamId, Model model) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
//        List<BaseFootballPlayer> baseFootballPlayers = (List<BaseFootballPlayer>) baseFootballPlayerRepository.findAll();
//        League league = footballTeam.getLeague();
//        List<FootballPlayer> footballPlayers = footballPlayerRepository.findByFootballTeam_League(league);
//        for (int i = 0; i < baseFootballPlayers.size(); i++) {
//            for (int j = 0; j < footballPlayers.size(); j++) {
//                if (baseFootballPlayers.get(i).getId().equals(footballPlayers.get(j).getBaseFootballPlayer().getId())){
//               baseFootballPlayers.remove(baseFootballPlayers.get(i));
//              }
//            }
//        }
//        FootballTeam footballTeam = footballTeamRepository.findById(teamId)
//        .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
//        List<BaseFootballPlayer> baseFootballPlayers = (List<BaseFootballPlayer>) baseFootballPlayerRepository.findAll();
//        League league = footballTeam.getLeague();
//        List<FootballPlayer> footballPlayers = footballPlayerRepository.findByFootballTeam_League(league);;
//// Create an iterator for baseFootballPlayers to safely remove elements while iterating
//        Iterator<BaseFootballPlayer> iterator = baseFootballPlayers.iterator();
//        while (iterator.hasNext()) {
//            BaseFootballPlayer baseFootballPlayer = iterator.next();
//            for (FootballPlayer footballPlayer : footballPlayers) {
//                if (baseFootballPlayer.getId().equals(footballPlayer.getBaseFootballPlayer().getId())) {
//                   iterator.remove(); // Remove the base football player
//                    break; // Exit the inner loop as the player is found
//                }
//            }
//        }
        model.addAttribute("footballTeam", footballTeam);
        model.addAttribute("allBaseFootballPlayers",baseFootballPlayerRepository.findAll());
        model.addAttribute("baseFootballPlayers",new ArrayList<BaseFootballPlayer>());
        return "/football-team/add-players";
    }
    public void chooseAwayFootballPlayersForSale(@PathVariable("teamId") Long teamId) {
        Random rand = new Random();
        int randomNumber = rand.nextInt(5) + 1;
        List<FootballTeam> allTeamsExceptCurrent = footballTeamRepository.findAllExceptTeamId(teamId);
        List<FootballPlayer> allFootballPlayersExceptCurrentTeam = new ArrayList<>();
        for (FootballTeam team : allTeamsExceptCurrent) {
            allFootballPlayersExceptCurrentTeam.addAll(getPlayersByTeamId(team.getId()));
        }
        List<FootballPlayer> selectedFootballPlayers = getRandomFootballPlayers(allFootballPlayersExceptCurrentTeam, randomNumber);
        for (FootballPlayer player : selectedFootballPlayers) {
            player.setFootballPlayerStatus(true);
            footballPlayerRepository.save(player);
        }
    }
    public List<FootballPlayer> getPlayersByTeamId(Long teamId) {
        Query query = entityManager.createQuery("SELECT p FROM FootballPlayer p WHERE p.footballTeam.id = :teamId");
        query.setParameter("teamId", teamId);
        return query.getResultList();
    }
    private List<FootballPlayer> getRandomFootballPlayers(List<FootballPlayer> players, int count) {
        List<FootballPlayer> randomPlayers = new ArrayList<>();
        Collections.shuffle(players);
        for (int i = 0; i < count && i < players.size(); i++) {
            randomPlayers.add(players.get(i));
        }
        return randomPlayers;
    }
    public String chooseFootballPlayersForSale(@PathVariable("teamId") Long teamId, Model model) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        model.addAttribute("footballTeam", footballTeam);
        model.addAttribute("allFootballPlayersOfTheTeam",getPlayersByTeamId(teamId));
        model.addAttribute("footballPlayersForSale",new ArrayList<FootballPlayer>());
        return "/football-team/sale-players";
    }
    public String getAllFootballPlayersForSale(Model model) {
        List<FootballPlayer> footballPlayers = (List<FootballPlayer>) footballPlayerRepository.findAll();
        model.addAttribute("allFootballPlayers",findPlayersByStatus(footballPlayers));
        return "/football-team/show-players-for-sale";
    }
    public String saleFootballPlayers(@RequestParam("teamId") Long teamId,Model model, @RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds){
        List<FootballPlayer> selectedFootballPlayer = footballPlayerRepository.findAllByIdIn(selectedFootballPlayerIds);
        for (int i = 0; i < selectedFootballPlayer.size(); i++) {
            selectedFootballPlayer.get(i).setFootballPlayerStatus(true);
            footballPlayerRepository.save(selectedFootballPlayer.get(i));
        }
        chooseAwayFootballPlayersForSale(teamId);
        return "redirect:/football-team/getFootballPlayersForSale";
    }
    public static List<FootballPlayer> findPlayersByStatus(List<FootballPlayer> players) {
        List<FootballPlayer> foundPlayers = new ArrayList<>();
        for (FootballPlayer player : players) {
            if (player.isFootballPlayerStatus()) {
                foundPlayers.add(player);
            }
        }
        return foundPlayers;
    }
    public String getAllFootballPlayers(Model model) {
        model.addAttribute("allFootballPlayers",footballPlayerRepository.findAll());
        return "/football-team/showFootballPlayers";
    }
    public String addFootballPlayersToTeam(@RequestParam("teamId") Long teamId, Model model, @RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds) {
        Optional<FootballTeam> footballTeamOptional = footballTeamRepository.findById(teamId);
        FootballTeam footballTeam = new FootballTeam();
        if (footballTeamOptional.isPresent()){
            footballTeam = footballTeamOptional.get();
        }
        TransferSumCalculator transferSumCalculator = new TransferSumCalculator();
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
            footballPlayer.setFootballPlayerStatus(false);
            int price = transferSumCalculator.getTransferSumForPlayer(footballPlayer);
            footballPlayer.setPrice(price);
            footballPlayerRepository.save(footballPlayer);
        }
        return "redirect:/football-team/getFootballPlayers";
    }

//    public List<FootballPlayer> findByLeagueId(League league) {
//        String jpql = "SELECT fp FROM FootballPlayer fp WHERE fp.footballTeam.league = :league";
//        TypedQuery<FootballPlayer> query = entityManager.createQuery(jpql, FootballPlayer.class);
//        query.setParameter("league", league);
//        return query.getResultList();
//    }
}
