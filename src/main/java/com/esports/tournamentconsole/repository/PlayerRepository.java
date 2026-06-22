package com.esports.tournamentconsole.repository;

import com.esports.tournamentconsole.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByTeam_TeamId(Long teamId);
}
