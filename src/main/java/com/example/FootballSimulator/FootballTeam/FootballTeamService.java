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
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.net.ssl.HandshakeCompletedEvent;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FootballTeamService {
    @Autowired
    private FootballPlayerRepository footballPlayerRepository;

    @Autowired
    private BaseFootballPlayerRepository baseFootballPlayerRepository;
    @Autowired
    private FootballTeamRepository footballTeamRepository;
    public String addPlayersToTeam(@PathVariable("teamId") Long teamId, Model model) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        model.addAttribute("footballTeam", footballTeam);
        model.addAttribute("allBaseFootballPlayers",removeSelectedPlayersFromBaseFPList(teamId));
        model.addAttribute("baseFootballPlayers",new ArrayList<BaseFootballPlayer>());
        return "/football-team/add-players";
    }
    public List<BaseFootballPlayer> removeSelectedPlayersFromBaseFPList(@PathVariable("teamId") Long teamId){
        List<BaseFootballPlayer> baseFootballPlayers = (List<BaseFootballPlayer>) baseFootballPlayerRepository.findAll();
        Optional<FootballTeam> footballTeam = footballTeamRepository.findById(teamId);
        League league = new League();
        if (footballTeam.isPresent()){
            league = footballTeam.get().getLeague();
        }
        List<FootballPlayer> footballPlayers = footballPlayerRepository.findByFootballTeam_LeagueId(league.getId());
        Set<Long> footballPlayerIds = footballPlayers.stream()
                .map(fp -> fp.getBaseFootballPlayer().getId())
                .collect(Collectors.toSet());
        baseFootballPlayers.removeIf(player -> footballPlayerIds.contains(player.getId()));
        return baseFootballPlayers;
    }

    public void chooseAwayFootballPlayersForSale(@RequestParam("teamId") Long teamId) {
        Random rand = new Random();
        int randomNumber = rand.nextInt(5) + 1;
        List<FootballTeam> allTeamsExceptCurrent = footballTeamRepository.findAllExceptTeamId(teamId);
        List<FootballPlayer> allFootballPlayersExceptCurrentTeam = new ArrayList<>();
        for (FootballTeam team : allTeamsExceptCurrent) {
            allFootballPlayersExceptCurrentTeam.addAll(footballPlayerRepository.getPlayersByTeamId(team.getId()));
        }
        List<FootballPlayer> selectedFootballPlayers = getRandomFootballPlayers(allFootballPlayersExceptCurrentTeam, randomNumber);
        for (FootballPlayer player : selectedFootballPlayers) {
            player.setFootballPlayerStatus(true);
            footballPlayerRepository.save(player);
        }
    }
    public void buyFootballPlayerForAwayTeam(@RequestParam("teamId") Long teamId){
        List<FootballPlayer> footballPlayers = footballPlayerRepository.getPlayersByTeamId(teamId);
        List<FootballPlayer> footballPlayersForSale = findPlayersByStatus(footballPlayers);
        List<FootballTeam> allTeamsExceptCurrent = footballTeamRepository.findAllExceptTeamId(teamId);
        List<FootballTeam> randomTeams = getRandomFootballTeam(allTeamsExceptCurrent,footballPlayersForSale.size());
        for (int i = 0; i < footballPlayersForSale.size(); i++) {
            randomTeams.get(i).setBudged(randomTeams.get(i).getBudged() - footballPlayersForSale.get(i).getPrice());
            footballPlayersForSale.get(i).getFootballTeam().setBudged(footballPlayersForSale.get(i).getFootballTeam().getBudged() + footballPlayersForSale.get(i).getPrice());
            footballPlayersForSale.get(i).setFootballTeam(randomTeams.get(i));
            footballPlayersForSale.get(i).setFootballPlayerStatus(false);
            footballPlayerRepository.save(footballPlayersForSale.get(i));
        }
    }
    private List<FootballPlayer> getRandomFootballPlayers(List<FootballPlayer> players, int count) {
        List<FootballPlayer> randomPlayers = new ArrayList<>();
        Collections.shuffle(players);
        for (int i = 0; i < count && i < players.size(); i++) {
            randomPlayers.add(players.get(i));
        }
        return randomPlayers;
    }
    private List<FootballTeam> getRandomFootballTeam(List<FootballTeam> teams, int count) {
        List<FootballTeam> randomTeams = new ArrayList<>();
        Collections.shuffle(teams);
        for (int i = 0; i < count && i < teams.size(); i++) {
            randomTeams.add(teams.get(i));
        }
        return randomTeams;
    }
    public String chooseFootballPlayersForSale(@PathVariable("teamId") Long teamId, Model model) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        model.addAttribute("footballTeam", footballTeam);
        model.addAttribute("allFootballPlayersOfTheTeam",footballPlayerRepository.getPlayersByTeamId(teamId));
        model.addAttribute("footballPlayersForSale",new ArrayList<FootballPlayer>());
        return "/football-team/sale-players";
    }
    public String getAllFootballPlayersForSale(@RequestParam("teamId") Long teamId,Model model) {
        List<FootballPlayer> footballPlayers = footballPlayerRepository.getPlayersByTeamId(teamId);
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
        return "redirect:/football-team/getFootballPlayersForSale?teamId=" + teamId;
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

    public String buyFootballPlayersForHomeTeam(@PathVariable("teamId") Long teamId,Model model){
        FootballTeam footballTeam = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        List<FootballPlayer> footballPlayersForSale = footballPlayerRepository.findForSalePlayersExceptTeamId(teamId);
        model.addAttribute("footballTeam", footballTeam);
        model.addAttribute("footballPlayersForSale", footballPlayersForSale);
        model.addAttribute("buyFootballPlayers", new ArrayList<FootballPlayer>());
        return "/football-team/buy-football-players";
    }

    public String showBoughtPlayers(@RequestParam("teamId") Long teamId,Model model){
        FootballTeam footballTeam = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        List<FootballPlayer> boughtFootballPlayers = footballPlayerRepository.getPlayersByTeamId(teamId);
        model.addAttribute("boughtFootballPlayers", boughtFootballPlayers);

        return "/football-team/show-bought-players";
    }

    public String buyFootballPlayer(@RequestParam("teamId") Long teamId,Model model,@RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds){
        FootballTeam footballTeam = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        List<FootballPlayer> boughtFootballPlayers = footballPlayerRepository.findAllByIdIn(selectedFootballPlayerIds);
        for (FootballPlayer player : boughtFootballPlayers) {
            if (footballTeam.getBudged() > player.getPrice()){
                footballTeam.setBudged(footballTeam.getBudged() - player.getPrice());
                player.getFootballTeam().setBudged(player.getFootballTeam().getBudged() + player.getPrice());
                player.setFootballTeam(footballTeam);
                player.setFootballPlayerStatus(false);
            }
            else{
                model.addAttribute("theBudgetIsNotEnough","The budget is not enough!");
            }
        }
        footballPlayerRepository.saveAll(boughtFootballPlayers);
        buyFootballPlayerForAwayTeam(teamId);
        return "redirect:/football-team/show-bought-players?teamId=" + teamId;
    }
    public String getAllFootballPlayers(@RequestParam("teamId") Long teamId,Model model) {
        model.addAttribute("allFootballPlayers",footballPlayerRepository.getPlayersByTeamId(teamId));
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
        return "redirect:/football-team/getFootballPlayers?teamId=" + teamId;
    }
}
