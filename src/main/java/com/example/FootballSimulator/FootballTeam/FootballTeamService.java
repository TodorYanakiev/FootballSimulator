package com.example.FootballSimulator.FootballTeam;

import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayer;
import com.example.FootballSimulator.BaseFootballPlayer.BaseFootballPlayerRepository;
import com.example.FootballSimulator.Constants.Status;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;
import com.example.FootballSimulator.FootballPlayer.FootballPlayerRepository;
import com.example.FootballSimulator.League.League;
import com.example.FootballSimulator.League.LeagueRepository;
import com.example.FootballSimulator.TransferSumCalculator;
import com.example.FootballSimulator.User.User;
import com.example.FootballSimulator.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    public String addPlayersToTeam(@PathVariable("teamId") Long teamId, Model model) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        if (!footballTeam.getLeague().getLeagueStatus().equals(Status.NOT_STARTED)) {
            return "redirect:/football-team/all/" + footballTeam.getLeague().getId();
        }
        List<BaseFootballPlayer> baseFootballPlayers = (List<BaseFootballPlayer>) baseFootballPlayerRepository.findAll();
        List<FootballPlayer> footballPlayers = footballPlayerRepository.getPlayersByTeamId(teamId);
        for (int i = 0; i < baseFootballPlayers.size(); i++) {
            for (int j = 0; j < footballPlayers.size(); j++) {
                if (baseFootballPlayers.get(i).getId().equals(footballPlayers.get(j).getBaseFootballPlayer().getId())) {
                    baseFootballPlayers.remove(baseFootballPlayers.get(i));
                }
            }
        }
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
        model.addAttribute("allBaseFootballPlayers", baseFootballPlayerRepository.findAll());
        model.addAttribute("baseFootballPlayers", new ArrayList<BaseFootballPlayer>());
        return "/football-team/add-players";
    }

    public void chooseAwayFootballPlayersForSale(@PathVariable("teamId") Long teamId) {
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

    public void buyFootballPlayerForAwayTeam(@PathVariable("teamId") Long teamId) {
        List<FootballPlayer> footballPlayers = footballPlayerRepository.getPlayersByTeamId(teamId);
        List<FootballPlayer> footballPlayersForSale = findPlayersByStatus(footballPlayers);
        List<FootballTeam> allTeamsExceptCurrent = footballTeamRepository.findAllExceptTeamId(teamId);
        List<FootballTeam> randomTeams = getRandomFootballTeam(allTeamsExceptCurrent, footballPlayersForSale.size());
        for (int i = 0; i < footballPlayersForSale.size(); i++) {
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
        model.addAttribute("allFootballPlayersOfTheTeam", footballPlayerRepository.getPlayersByTeamId(teamId));
        model.addAttribute("footballPlayersForSale", new ArrayList<FootballPlayer>());
        return "/football-team/sale-players";
    }

    public String getAllFootballPlayersForSale(@RequestParam("teamId") Long teamId, Model model) {
        List<FootballPlayer> footballPlayers = footballPlayerRepository.getPlayersByTeamId(teamId);
        model.addAttribute("allFootballPlayers", findPlayersByStatus(footballPlayers));
        return "/football-team/show-players-for-sale";
    }

    public String saleFootballPlayers(@RequestParam("teamId") Long teamId, Model model, @RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds) {
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

    public String buyFootballPlayersForHomeTeam(@PathVariable("teamId") Long teamId, Model model) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        List<FootballPlayer> footballPlayersForSale = footballPlayerRepository.findForSalePlayersExceptTeamId(teamId);
        model.addAttribute("footballTeam", footballTeam);
        model.addAttribute("footballPlayersForSale", footballPlayersForSale);
        model.addAttribute("buyFootballPlayers", new ArrayList<FootballPlayer>());
        return "/football-team/buy-football-players";
    }

    public String showBoughtPlayers(@RequestParam("teamId") Long teamId, Model model) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        List<FootballPlayer> boughtFootballPlayers = footballPlayerRepository.getPlayersByTeamId(teamId);
        model.addAttribute("boughtFootballPlayers", boughtFootballPlayers);

        return "/football-team/show-bought-players";
    }

    public String buyFootballPlayer(@RequestParam("teamId") Long teamId, Model model, @RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds) {
        FootballTeam footballTeam = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        List<FootballPlayer> boughtFootballPlayers = footballPlayerRepository.findAllByIdIn(selectedFootballPlayerIds);
        for (FootballPlayer player : boughtFootballPlayers) {
            player.setFootballTeam(footballTeam);
            player.setFootballPlayerStatus(false);
        }
        footballPlayerRepository.saveAll(boughtFootballPlayers);
        buyFootballPlayerForAwayTeam(teamId);
        return "redirect:/football-team/show-bought-players?teamId=" + teamId;
    }

    public String getAllFootballPlayers(@RequestParam("teamId") Long teamId, Model model) {
        model.addAttribute("allFootballPlayers", footballPlayerRepository.getPlayersByTeamId(teamId));
        return "/football-team/showFootballPlayers";
    }

    public String addFootballPlayersToTeam(@RequestParam("teamId") Long teamId, Model model, @RequestParam("selectedFootballPlayersIds") List<Long> selectedFootballPlayerIds) {
        Optional<FootballTeam> footballTeamOptional = footballTeamRepository.findById(teamId);
        FootballTeam footballTeam = new FootballTeam();
        if (footballTeamOptional.isPresent()) {
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

    //    public List<FootballPlayer> findByLeagueId(League league) {
//        String jpql = "SELECT fp FROM FootballPlayer fp WHERE fp.footballTeam.league = :league";
//        TypedQuery<FootballPlayer> query = entityManager.createQuery(jpql, FootballPlayer.class);
//        query.setParameter("league", league);
//        return query.getResultList();
//    }

    public String viewTeam(Long teamId, Model model) {
        Optional<FootballTeam> optionalFootballTeam = footballTeamRepository.findById(teamId);
        if (optionalFootballTeam.isPresent()) {
            model.addAttribute("footballTeam", optionalFootballTeam.get());
            return "/football-team/view";
        } else return "/home";
    }

    public String selectTeam(Long teamId, Model model) {
        Optional<FootballTeam> optionalFootballTeam = footballTeamRepository.findById(teamId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(authentication.getName());
        if (user.getFootballTeam() != null) {
            model.addAttribute("message", "You can not manage more than 1 team!");
            model.addAttribute("footballTeam", user.getFootballTeam());
            return "/football-team/view";
        }
        if (optionalFootballTeam.isPresent()) {
            FootballTeam footballTeam = optionalFootballTeam.get();
            if (footballTeam.getUser() != null) {
                model.addAttribute("message", "This team is already selected!");
                return "redirect:/football-team/all/" + footballTeam.getLeague().getId();
            }
            user.setFootballTeam(footballTeam);
            userRepository.save(user);
            footballTeam.setUser(user);
            footballTeamRepository.save(footballTeam);
            model.addAttribute("footballTeam", footballTeam);
        } else {
            return "redirect:/";
        }
        return "/football-team/view";
    }

    public String viewAllTeamsByLeague(Long leagueId, Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.getUserByUsername(authentication.getName());
        Optional<League> optionalLeague = leagueRepository.findById(leagueId);
        if (optionalLeague.isEmpty()) return "redirect:/league/get";
        League league = optionalLeague.get();
        List<FootballTeam> footballTeamList = league.getFootballTeamList();
//        if (user.getRole().equals(Role.ROLE_USER)) {
//            List<FootballTeam> availableTeams = footballTeamList.stream()
//                    .filter(footballTeam -> footballTeam.getUser() == null).collect(Collectors.toList());
//            model.addAttribute("league", league);
//            model.addAttribute("footballTeams", availableTeams);
//        } else{
//            model.addAttribute("league", league);
//            model.addAttribute("footballTeams", footballTeamList);
//        }
        model.addAttribute("league", league);
        model.addAttribute("footballTeams", footballTeamList);
        return "/football-team/teams-for-league";
    }

    public String viewAllAvailableTeamsByLeague(Long leagueId, Model model) {
        Optional<League> optionalLeague = leagueRepository.findById(leagueId);
        if (optionalLeague.isEmpty()) return "redirect:/league/get";
        League league = optionalLeague.get();
        List<FootballTeam> footballTeamList = league.getFootballTeamList();
        List<FootballTeam> availableTeams = footballTeamList.stream()
                .filter(footballTeam -> footballTeam.getUser() == null).collect(Collectors.toList());
        model.addAttribute("league", league);
        model.addAttribute("footballTeams", availableTeams);
        return "/football-team/available-teams-for-league";
    }
}