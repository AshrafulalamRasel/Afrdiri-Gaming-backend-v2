package com.afridi.gamingbackend.services;


import com.afridi.gamingbackend.domain.model.PlayersEntity;
import com.afridi.gamingbackend.domain.model.Role;
import com.afridi.gamingbackend.domain.model.RoleName;
import com.afridi.gamingbackend.domain.repository.AdminRepository;
import com.afridi.gamingbackend.domain.repository.RoleRepository;
import com.afridi.gamingbackend.domain.repository.UserRepository;
import com.afridi.gamingbackend.dto.request.ChangePasswordRequest;
import com.afridi.gamingbackend.dto.request.LoginForm;
import com.afridi.gamingbackend.dto.request.UserSignUpForm;
import com.afridi.gamingbackend.dto.response.JwtResponse;
import com.afridi.gamingbackend.exceptionHandalling.ResourceNotFoundException;
import com.afridi.gamingbackend.security.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SignUpAndSignInService {

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminRepository adminRepository;

    public ResponseEntity<String> AdminSignUp(UserSignUpForm signUpRequest) {
        Set<String> role = new HashSet<>();
        role.add("SUPER_ADMIN");

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<String>("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Fail -> Email is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        PlayersEntity adminClass = new PlayersEntity();

        UUID id = UUID.randomUUID();
        String uuid = id.toString();
        adminClass.setId(uuid);
        adminClass.setUsername(signUpRequest.getUsername());
        adminClass.setEmail(signUpRequest.getEmail());
        adminClass.setPassword(encoder.encode(signUpRequest.getPassword()));
        adminClass.setRoles(getRolesOrThrow(role));

        userRepository.saveAndFlush(adminClass);
        return new ResponseEntity<String>(uuid, HttpStatus.OK);
    }


    public ResponseEntity<String> CommonUserSignUp(UserSignUpForm signUpRequest) {
        Set<String> role = new HashSet<>();
        role.add("USER");

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<String>("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Fail -> Email is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        PlayersEntity playersEntity = new PlayersEntity();

        UUID id = UUID.randomUUID();
        String uuid = id.toString();
        playersEntity.setId(uuid);
        playersEntity.setUsername(signUpRequest.getUsername());
        playersEntity.setEmail(signUpRequest.getEmail());
        playersEntity.setPassword(encoder.encode(signUpRequest.getPassword()));
        playersEntity.setRoles(getRolesOrThrow(role));

        userRepository.saveAndFlush(playersEntity);
        return new ResponseEntity<String>(uuid, HttpStatus.OK);

    }


    public JwtResponse CommonUserBothSignIn(LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        return new JwtResponse(jwt);
    }

    public String getLoggedAuthUser() {

        Object authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<String> loggedInAuthUserId = null;
        String loggedInAuthUser = null;

        if (authUser instanceof UserDetails) {

            String username = ((UserDetails) authUser).getUsername();
            loggedInAuthUserId = userRepository.findAuthIdByUserName(username);
            loggedInAuthUser = username;

        } else if (authUser instanceof UserDetails == false) {
            throw new RuntimeException("LoggedIn user does not  account.");

        } else {
            String username = authUser.toString();

            System.out.println(username);
        }
        // return new ResponseEntity<String>("User Name "+"\t\t"+ loggedInAuthUser+"\n" +"LoggedUserId "+"\t"+loggedInAuthUserId.get(), HttpStatus.OK);
        return loggedInAuthUserId.get();

    }


    private Set<Role> getRolesOrThrow(Set<String> roles2) {
        Set<Role> roles = new HashSet<>();
        for (String role : roles2) {
            Optional<Role> roleOptional = roleRepository.findByName(RoleName.valueOf(role));
            System.out.println(roleOptional.get());
            if (!roleOptional.isPresent()) {
                throw new ValidationException("Role '" + role + "' does not exist.");
            }
            roles.add(roleOptional.get());
        }
        return roles;
    }

    public String updatePasswordCommon(ChangePasswordRequest changePasswordRequest) {

        String uuid = getLoggedAuthUser();

        Optional<PlayersEntity> userClassOptional = userRepository.findAllById(uuid);
        if (!userClassOptional.isPresent()) {
            throw new ResourceNotFoundException("UserName not found.");
        }

        PlayersEntity playersEntity = userClassOptional.get();
        playersEntity.setPassword(encoder.encode(changePasswordRequest.getPassword()));
        userRepository.save(playersEntity);

        return "Successfully done";

    }


}
