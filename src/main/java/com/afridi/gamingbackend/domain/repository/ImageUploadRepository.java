package com.afridi.gamingbackend.domain.repository;


import com.afridi.gamingbackend.domain.model.ImageUploadHeader;
import com.afridi.gamingbackend.domain.model.PlayersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageUploadRepository extends JpaRepository<ImageUploadHeader, String> {

    Optional<ImageUploadHeader> findAllById(String id);

    @Override
    long count();
}
