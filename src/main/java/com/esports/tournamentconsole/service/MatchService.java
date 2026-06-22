package com.esports.tournamentconsole.service;

import com.esports.tournamentconsole.model.Match;
import com.esports.tournamentconsole.model.Team;
import com.esports.tournamentconsole.repository.MatchRepository;
import com.esports.tournamentconsole.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    public List<Match> findUpcoming() {
        return matchRepository.findByWinnerTeamIsNullOrderByScheduledTimeAsc();
    }

    public Match findById(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));
    }

    @Transactional
    public Match updateScore(Long matchId, Long winnerTeamId, String score) {
        Match match = findById(matchId);
        Long t1 = match.getTeam1().getTeamId();
        Long t2 = match.getTeam2().getTeamId();
        
        if (!winnerTeamId.equals(t1) && !winnerTeamId.equals(t2)) {
            throw new IllegalArgumentException("Winner must be one of the two competing teams.");
        }
        
        Team winner = teamRepository.findById(winnerTeamId)
                .orElseThrow(() -> new IllegalArgumentException("Winner team not found"));
                
        match.setWinnerTeam(winner);
        match.setScore(score);
        return matchRepository.save(match);
    }

    public List<Map<String, Object>> buildLeaderboard() {
        Map<Long, Map<String, Object>> board = new LinkedHashMap<>();
        
        // Seed every team with 0 wins first
        for (Team t : teamRepository.findAll()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("teamId", t.getTeamId());
            row.put("teamName", t.getTeamName());
            row.put("region", t.getRegion());
            row.put("wins", 0L);
            board.put(t.getTeamId(), row);
        }
        
        // Overlay actual win counts from the aggregation query
        for (Object[] result : matchRepository.countWinsPerTeam()) {
            Long teamId = (Long) result[0];
            Long wins = (Long) result[2];
            if (board.containsKey(teamId)) {
                board.get(teamId).put("wins", wins);
            }
        }
        
        List<Map<String, Object>> leaderboard = new ArrayList<>(board.values());
        leaderboard.sort((a, b) -> Long.compare((Long) b.get("wins"), (Long) a.get("wins")));
        return leaderboard;
    }
}
