package com.afridi.gamingbackend.domain.repository;


import com.afridi.gamingbackend.domain.model.RegisterUsersInGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationUsersInGameRepository extends JpaRepository<RegisterUsersInGameEntity, Long> {

    Optional<List<RegisterUsersInGameEntity>> findAllByUserId(String id);

    @Query(
            value = "SELECT * FROM  register_users_in_game_entity WHERE f_key = ? AND user_id = ?",
            nativeQuery = true)
    Optional<RegisterUsersInGameEntity> findAllByUserIdAndPlayerId(@Param("f_key") String f_key,@Param("user_id") String user_id);


}
