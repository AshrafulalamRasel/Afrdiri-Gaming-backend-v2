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
            value = "SELECT * FROM  REGESTART_USER_INGAME_PROFILE WHERE f_key = ? AND user_id = ?",
            nativeQuery = true)
    Optional<RegisterUsersInGameEntity> findAllByUserIdAndPlayerId(@Param("f_key") String f_key,@Param("user_id") String user_id);


    @Query(value = "SELECT * FROM  REGESTART_USER_INGAME_PROFILE WHERE f_key = ?",nativeQuery = true)
    Optional<List<RegisterUsersInGameEntity>> findAllUserBygameId(@Param("f_key") String f_key);

    @Query(value = "SELECT * FROM  REGESTART_USER_INGAME_PROFILE WHERE user_id = ?",nativeQuery = true)
    Optional<List<RegisterUsersInGameEntity>> findAllGameByUserId(@Param("user_id") String userId);


    @Query(value = "SELECT COUNT(*) FROM REGESTART_USER_INGAME_PROFILE  WHERE f_key=?",nativeQuery = true)
    long countAllByGameIdStatus(@Param("f_key") String f_key);
}
