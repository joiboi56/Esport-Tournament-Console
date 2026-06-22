package com.esports.tournamentconsole.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "PLAYERS")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAYER_ID")
    private Long playerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", nullable = false)
    private Team team;

    @NotBlank(message = "Game handle is required")
    @Column(name = "GAME_HANDLE", nullable = false)
    private String gameHandle;

    @NotBlank(message = "In-game role is required")
    @Column(name = "IN_GAME_ROLE", nullable = false)
    private String inGameRole;

    public Player() {}

    // --- Getters and Setters ---
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public String getGameHandle() { return gameHandle; }
    public void setGameHandle(String gameHandle) { this.gameHandle = gameHandle; }

    public String getInGameRole() { return inGameRole; }
    public void setInGameRole(String inGameRole) { this.inGameRole = inGameRole; }
}
