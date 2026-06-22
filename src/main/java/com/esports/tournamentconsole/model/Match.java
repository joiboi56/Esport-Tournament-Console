package com.esports.tournamentconsole.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MATCHES")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MATCH_ID")
    private Long matchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM1_ID", nullable = false)
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM2_ID", nullable = false)
    private Team team2;

    @Column(name = "SCHEDULED_TIME", nullable = false)
    private LocalDateTime scheduledTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WINNER_TEAM_ID")
    private Team winnerTeam;

    @Column(name = "SCORE")
    private String score;

    public Match() {}

    // --- Getters and Setters ---
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public Team getTeam1() { return team1; }
    public void setTeam1(Team team1) { this.team1 = team1; }

    public Team getTeam2() { return team2; }
    public void setTeam2(Team team2) { this.team2 = team2; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public Team getWinnerTeam() { return winnerTeam; }
    public void setWinnerTeam(Team winnerTeam) { this.winnerTeam = winnerTeam; }

    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }
}
