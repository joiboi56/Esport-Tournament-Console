package com.esports.tournamentconsole.repository;

import com.esports.tournamentconsole.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);
    boolean existsByTeamName(String teamName);
    List<Team> findAllByOrderByTeamIdAsc();
}
