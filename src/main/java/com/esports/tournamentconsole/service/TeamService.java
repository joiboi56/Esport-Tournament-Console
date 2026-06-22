package com.esports.tournamentconsole.service;

import com.esports.tournamentconsole.model.Match;
import com.esports.tournamentconsole.model.Player;
import com.esports.tournamentconsole.model.Team;
import com.esports.tournamentconsole.repository.MatchRepository;
import com.esports.tournamentconsole.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public TeamService(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team findById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));
    }

    @Transactional
    public Team registerTeamWithRoster(Team team, List<Player> roster) {
        if (teamRepository.existsByTeamName(team.getTeamName())) {
            throw new IllegalArgumentException("Team name already exists: " + team.getTeamName());
        }
        for (Player p : roster) {
            p.setTeam(team); // set both sides of the relationship
            team.getPlayers().add(p);
        }
        // cascade = ALL on Team.players means saving 'team' also saves the roster
        return teamRepository.save(team);
    }

    public List<Match> findMatchesForTeam(Long teamId) {
        return matchRepository.findAllMatchesInvolvingTeam(teamId);
    }

    @Transactional
    public void deleteTeamCascade(Long teamId) {
        Team team = findById(teamId);
        List<Match> linkedMatches = matchRepository.findAllMatchesInvolvingTeam(teamId);

        if (!linkedMatches.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete team '" + team.getTeamName() + "': "
                            + linkedMatches.size() + " match(es) still reference this team. "
                            + "Remove or reassign those matches first.");
        }
        // Players are removed automatically via cascade = ALL + orphanRemoval = true
        teamRepository.delete(team);
    }
}