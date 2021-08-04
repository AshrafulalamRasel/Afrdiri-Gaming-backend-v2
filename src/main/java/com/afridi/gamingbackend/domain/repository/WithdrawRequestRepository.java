package com.afridi.gamingbackend.domain.repository;


import com.afridi.gamingbackend.domain.model.MoneyRequestEntity;
import com.afridi.gamingbackend.domain.model.MoneyWithdrawRequestEntity;
import com.afridi.gamingbackend.dto.request.WithDrawMoneyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawRequestRepository extends JpaRepository<MoneyWithdrawRequestEntity, String> {

    List<MoneyWithdrawRequestEntity> findAllByUserId(String id);

}
