package com.afridi.gamingbackend.services;


import com.afridi.gamingbackend.domain.model.AdminEntity;
import com.afridi.gamingbackend.domain.model.PlayStoreLink;
import com.afridi.gamingbackend.domain.model.PlayersEntity;
import com.afridi.gamingbackend.domain.repository.AdminRepository;
import com.afridi.gamingbackend.domain.repository.PlaystoreLinkRepository;
import com.afridi.gamingbackend.domain.repository.UserRepository;
import com.afridi.gamingbackend.dto.request.AdminInfoRequest;
import com.afridi.gamingbackend.dto.request.PlayStoreLInkRequest;
import com.afridi.gamingbackend.dto.response.AdminInfoResponse;
import com.afridi.gamingbackend.dto.response.IdentityResponse;
import com.afridi.gamingbackend.exceptionHandalling.ResourceNotFoundException;
import com.afridi.gamingbackend.security.jwt.JwtProvider;
import com.afridi.gamingbackend.util.UuidUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PlaystoreLinkService {

    private final PlaystoreLinkRepository playstoreLinkRepository;
    private final UuidUtil uuidUtil;

    public IdentityResponse createPlayStoreLink(PlayStoreLInkRequest playStoreLInkRequest) {

        String uuid = uuidUtil.getUuid();

        PlayStoreLink playStoreLink = new PlayStoreLink();

        playStoreLink.setId(uuid);
        playStoreLink.setVersionNo(playStoreLInkRequest.getVersionNo());
        playStoreLink.setPlayStoreLink(playStoreLInkRequest.getPlayStoreLink());

        playstoreLinkRepository.saveAndFlush(playStoreLink);

        return new IdentityResponse(uuid);

    }


    public List<PlayStoreLInkRequest> getStoreLink(){

        List<PlayStoreLInkRequest> playStoreLInkRequestList = new ArrayList<>();

        List<PlayStoreLink> playStoreLInkRequest = playstoreLinkRepository.findAll();

        for (PlayStoreLink playStoreLink : playStoreLInkRequest){

            PlayStoreLInkRequest playStoreLInkRequest1  = new PlayStoreLInkRequest();

            playStoreLInkRequest1.setVersionNo(playStoreLink.getVersionNo());
            playStoreLInkRequest1.setPlayStoreLink(playStoreLink.getPlayStoreLink());

            playStoreLInkRequestList.add(playStoreLInkRequest1);

        }

        return playStoreLInkRequestList;

    }


}
