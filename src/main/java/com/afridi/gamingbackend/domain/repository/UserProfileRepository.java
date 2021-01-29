package com.afridi.gamingbackend.domain.repository;


import com.afridi.gamingbackend.domain.model.PlayersProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<PlayersProfileEntity, String> {

    Optional<PlayersProfileEntity> findAllById(String id);


}
