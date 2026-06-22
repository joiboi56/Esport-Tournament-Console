package com.esports.tournamentconsole.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TEAMS")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long teamId;

    @NotBlank(message = "Team name is required")
    @Column(name = "TEAM_NAME", nullable = false, unique = true)
    private String teamName;

    @NotBlank(message = "Region is required")
    @Column(name = "REGION", nullable = false)
    private String region;

    @NotBlank(message = "Captain name is required")
    @Column(name = "CAPTAIN_NAME", nullable = false)
    private String captainName;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    // --- Constructors ---
    public Team() {}

    // --- Getters and Setters ---
    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getCaptainName() { return captainName; }
    public void setCaptainName(String captainName) { this.captainName = captainName; }

    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }
}
