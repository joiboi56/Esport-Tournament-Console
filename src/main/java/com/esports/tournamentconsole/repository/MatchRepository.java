package com.esports.tournamentconsole.repository;

import com.esports.tournamentconsole.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    // Upcoming = no winner recorded yet, ordered soonest first
    List<Match> findByWinnerTeamIsNullOrderByScheduledTimeAsc();

    // Any match (past or future) involving this team — used by the delete-team check
    @Query("SELECT m FROM Match m WHERE m.team1.teamId = :teamId " +
           "OR m.team2.teamId = :teamId OR m.winnerTeam.teamId = :teamId")
    List<Match> findAllMatchesInvolvingTeam(Long teamId);

    // Leaderboard: count of wins per team, highest first
    @Query("SELECT m.winnerTeam.teamId, m.winnerTeam.teamName, COUNT(m) " +
           "FROM Match m WHERE m.winnerTeam IS NOT NULL " +
           "GROUP BY m.winnerTeam.teamId, m.winnerTeam.teamName " +
           "ORDER BY COUNT(m) DESC")
    List<Object[]> countWinsPerTeam();
}
