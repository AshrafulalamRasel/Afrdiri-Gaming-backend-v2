package com.afridi.gamingbackend.domain.repository;


import com.afridi.gamingbackend.domain.model.PlayersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<PlayersEntity, String> {

    Optional<PlayersEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT id FROM player_admin_signup WHERE username = ?1", nativeQuery = true)
    Optional<String> findAuthIdByUserName(String username);

    Optional<PlayersEntity> findAllById(String id);

    Optional<PlayersEntity> findAllByEmail(String userName);

}
