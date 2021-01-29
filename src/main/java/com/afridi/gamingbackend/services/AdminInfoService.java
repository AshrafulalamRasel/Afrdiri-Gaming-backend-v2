package com.afridi.gamingbackend.services;


import com.afridi.gamingbackend.domain.model.AdminEntity;
import com.afridi.gamingbackend.domain.model.PlayersEntity;
import com.afridi.gamingbackend.domain.repository.AdminRepository;
import com.afridi.gamingbackend.domain.repository.UserRepository;
import com.afridi.gamingbackend.dto.request.AdminInfoRequest;
import com.afridi.gamingbackend.dto.response.AdminInfoResponse;
import com.afridi.gamingbackend.exceptionHandalling.ResourceNotFoundException;
import com.afridi.gamingbackend.security.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AdminInfoService {

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignUpAndSignInService signUpAndSignInService;

    @Autowired
    private AdminRepository adminRepository;

    public ResponseEntity<String> createAdminInformation(AdminInfoRequest adminInfoRequest) {

        String uuid = signUpAndSignInService.getLoggedAuthUser();
        Optional<AdminEntity> adminClassOptional = adminRepository.findAllById(uuid);

     /*   adminRepository.findAllById(uuid)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with -> username or email : " + uuid)
                );
*/

        if (!adminClassOptional.isPresent()) {

            AdminEntity adminEntity = new AdminEntity();

            adminEntity.setId(uuid);
            adminEntity.setFirstName(adminInfoRequest.getFirstName());
            adminEntity.setLastName(adminInfoRequest.getLastName());
            adminEntity.setMobileNo(adminInfoRequest.getMobileNo());
            adminEntity.setIsActive(false);
            adminRepository.saveAndFlush(adminEntity);
        }

        else if (adminClassOptional.isPresent()){
            throw new ResourceNotFoundException("Already Present: ");

        }



       return new ResponseEntity<String>(uuid, HttpStatus.CREATED);
    }

    public List<AdminInfoResponse> getAdminInformation() {

        String uuid = signUpAndSignInService.getLoggedAuthUser();
        List<AdminInfoResponse> adminInfoResponseList = new ArrayList<>();

        Optional<PlayersEntity> userClassOptional = userRepository.findAllById(uuid);
        Optional<AdminEntity> adminClassOptional = adminRepository.findAllById(uuid);

        AdminEntity adminEntity = adminClassOptional.get();
        PlayersEntity playersEntity = userClassOptional.get();

        AdminInfoResponse adminInfoResponse = new AdminInfoResponse();

        adminInfoResponse.setFirstName(adminEntity.getFirstName());
        adminInfoResponse.setLastName(adminEntity.getLastName());
        adminInfoResponse.setUsername(playersEntity.getUsername());
        adminInfoResponse.setEmail(playersEntity.getEmail());
        adminInfoResponse.setMobileNo(adminEntity.getMobileNo());

        adminInfoResponseList.add(adminInfoResponse);
        return adminInfoResponseList;
    }

}
