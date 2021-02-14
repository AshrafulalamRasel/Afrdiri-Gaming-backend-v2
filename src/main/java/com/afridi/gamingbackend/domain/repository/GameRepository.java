package com.afridi.gamingbackend.domain.repository;


import com.afridi.gamingbackend.domain.model.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, String> {


    Optional<GameEntity> findAllById(String id);




    //    @Query(
//            value = "SELECT * FROM GAME_SERVICE u WHERE u.isactive = 1",
//            nativeQuery = true)
//    List<GameEntity> findAllActiveGameNative();
//
//
//    @Query(
//            value = "SELECT * FROM GAME_SERVICE u WHERE u.isactive = 0",
//            nativeQuery = true)
//    List<GameEntity> findAllInActiveGameNative();
    @Query(value = "SELECT * FROM game_entity WHERE isactive = true", nativeQuery = true)
    List<GameEntity> findAllActiveGameNative();

    @Query(value = "SELECT * FROM game_entity WHERE isactive = false ", nativeQuery = true)
    List<GameEntity> findAllInActiveGameNative();

    Optional<List<GameEntity>> findAllByGameOwnerId(String id);


}
