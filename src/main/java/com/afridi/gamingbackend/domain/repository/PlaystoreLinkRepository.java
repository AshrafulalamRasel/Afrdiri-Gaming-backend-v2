package com.afridi.gamingbackend.domain.repository;


import com.afridi.gamingbackend.domain.model.AdminEntity;
import com.afridi.gamingbackend.domain.model.PlayStoreLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaystoreLinkRepository extends JpaRepository<PlayStoreLink, String> {

    Optional<PlayStoreLink> findAllById(String id);

}
