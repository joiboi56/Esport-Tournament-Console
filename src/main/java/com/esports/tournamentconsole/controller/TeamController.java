package com.esports.tournamentconsole.controller;

import com.esports.tournamentconsole.model.Player;
import com.esports.tournamentconsole.model.Team;
import com.esports.tournamentconsole.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public String listTeams(Model model) {
        model.addAttribute("teams", teamService.findAll());
        return "teams-list";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        Team team = new Team();
        // Pre-populate 3 empty player rows for the roster form
        for (int i = 0; i < 3; i++) team.getPlayers().add(new Player());
        model.addAttribute("team", team);
        return "register-team";
    }

    @PostMapping("/register")
    public String registerTeam(@Valid @ModelAttribute("team") Team team,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register-team"; // redisplay form with validation errors
        }
        try {
            teamService.registerTeamWithRoster(team, new ArrayList<>(team.getPlayers()));
            redirectAttributes.addFlashAttribute("successMessage",
                    "Team '" + team.getTeamName() + "' registered successfully!");
            return "redirect:/teams";
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("teamName", "duplicate", ex.getMessage());
            return "register-team";
        }
    }

    @GetMapping("/{id}/delete")
    public String confirmDelete(@PathVariable("id") Long id, Model model) {
        model.addAttribute("team", teamService.findById(id));
        model.addAttribute("linkedMatches", teamService.findMatchesForTeam(id));
        return "delete-team";
    }

    @PostMapping("/{id}/delete")
    public String deleteTeam(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            teamService.deleteTeamCascade(id);
            redirectAttributes.addFlashAttribute("successMessage", "Team removed successfully.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/teams";
    }
}