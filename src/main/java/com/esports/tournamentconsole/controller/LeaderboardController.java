package com.esports.tournamentconsole.controller;

import com.esports.tournamentconsole.service.MatchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LeaderboardController {
    private final MatchService matchService;

    public LeaderboardController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        model.addAttribute("leaderboard", matchService.buildLeaderboard());
        return "leaderboard";
    }
}
