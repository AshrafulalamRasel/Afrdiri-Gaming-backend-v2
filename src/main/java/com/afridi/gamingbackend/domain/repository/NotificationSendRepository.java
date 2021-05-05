package com.afridi.gamingbackend.domain.repository;

import com.afridi.gamingbackend.domain.model.NotificationSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationSendRepository extends JpaRepository<NotificationSend,String> {
}
