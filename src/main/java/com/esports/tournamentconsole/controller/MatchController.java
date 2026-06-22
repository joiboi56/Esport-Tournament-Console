package com.esports.tournamentconsole.controller;

import com.esports.tournamentconsole.service.MatchService;
import com.esports.tournamentconsole.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;
    private final TeamService teamService;

    public MatchController(MatchService matchService, TeamService teamService) {
        this.matchService = matchService;
        this.teamService = teamService;
    }

    @GetMapping("/upcoming")
    public String upcomingMatches(Model model) {
        model.addAttribute("matches", matchService.findUpcoming());
        return "schedule";
    }

    @GetMapping("/{id}/update-score")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("match", matchService.findById(id));
        return "update-score";
    }

    @PostMapping("/{id}/update-score")
    public String updateScore(@PathVariable("id") Long id,
                              @RequestParam("winnerTeamId") Long winnerTeamId,
                              @RequestParam("score") String score,
                              RedirectAttributes redirectAttributes) {
        try {
            matchService.updateScore(id, winnerTeamId, score);
            redirectAttributes.addFlashAttribute("successMessage", "Match result saved.");
            return "redirect:/leaderboard";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/matches/" + id + "/update-score";
        }
    }
}