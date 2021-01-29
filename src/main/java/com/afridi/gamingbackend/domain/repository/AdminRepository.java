package com.afridi.gamingbackend.domain.repository;


import com.afridi.gamingbackend.domain.model.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, String> {
    Optional<AdminEntity> findAllById(String id);

}
