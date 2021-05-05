package com.afridi.gamingbackend.domain.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NOTIFICATIONSEND")
public class NotificationSend extends BaseEntity {

    private String notificationSubject;
    private String notificationBody;

}
