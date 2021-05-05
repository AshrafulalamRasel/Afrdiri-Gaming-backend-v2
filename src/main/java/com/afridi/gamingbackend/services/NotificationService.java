package com.afridi.gamingbackend.services;

import com.afridi.gamingbackend.domain.model.NotificationSend;
import com.afridi.gamingbackend.domain.repository.NotificationSendRepository;
import com.afridi.gamingbackend.dto.request.NotificationRequest;
import com.afridi.gamingbackend.dto.response.IdentityResponse;
import com.afridi.gamingbackend.util.UuidUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class NotificationService {

    private final NotificationSendRepository notificationSendRepository;

    private final UuidUtil uuidUtil;


    public IdentityResponse createNotification(NotificationRequest notificationRequest) {

        String id = uuidUtil.getUuid();

        List<NotificationSend> notificationSendList = notificationSendRepository.findAll();

        if (notificationSendList.isEmpty()) {

            NotificationSend notificationSend = new NotificationSend();

            notificationSend.setId(id);
            notificationSend.setNotificationSubject(notificationRequest.getNotificationSubject());
            notificationSend.setNotificationBody(notificationRequest.getNotificationBody());
            notificationSendRepository.saveAndFlush(notificationSend);

        } else {

            for (NotificationSend notificationSendl : notificationSendList) {

                notificationSendRepository.delete(notificationSendl);

                NotificationSend notificationSend = new NotificationSend();
                notificationSend.setId(id);
                notificationSend.setNotificationSubject(notificationRequest.getNotificationSubject());
                notificationSend.setNotificationBody(notificationRequest.getNotificationBody());
                notificationSendRepository.saveAndFlush(notificationSend);
            }
        }

        return new IdentityResponse(id);
    }


    public List<NotificationRequest> getNotificationList(){

        List<NotificationSend> notificationSendList = notificationSendRepository.findAll();

        List<NotificationRequest> notificationRequestList = new ArrayList<>();

        for (NotificationSend notificationSend: notificationSendList){

            NotificationRequest notificationRequest = new NotificationRequest();

            notificationRequest.setNotificationSubject(notificationSend.getNotificationSubject());
            notificationRequest.setNotificationBody(notificationSend.getNotificationBody());
            notificationRequestList.add(notificationRequest);
            notificationSendRepository.delete(notificationSend);

        }
        return notificationRequestList;



    }

}
