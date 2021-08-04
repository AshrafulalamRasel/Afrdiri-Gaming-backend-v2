package com.afridi.gamingbackend.domain.repository;


import com.afridi.gamingbackend.domain.model.MoneyRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyRequestRepository extends JpaRepository<MoneyRequestEntity, String> {

    @Query(
            value = "SELECT * FROM MONEY_REACHARGE_ACCOUNT WHERE is_authority_processed = true",
            nativeQuery = true)

    List<MoneyRequestEntity> findAllActiveBalanceRequest();

    @Query(
            value = "SELECT * FROM MONEY_REACHARGE_ACCOUNT WHERE is_authority_processed = false",
            nativeQuery = true)

    List<MoneyRequestEntity> findAllInActiveBalanceRequest();
    List<MoneyRequestEntity> findAllByUserId(String id);

}
