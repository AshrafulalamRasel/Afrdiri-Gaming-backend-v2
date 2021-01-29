package com.afridi.gamingbackend.domain.repository;


import com.afridi.gamingbackend.domain.model.PlayingPartnerDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayingPartnerDetailsRepository extends JpaRepository<PlayingPartnerDetailsEntity, Long> {

}
