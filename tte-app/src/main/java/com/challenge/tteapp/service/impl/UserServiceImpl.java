package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.processor.JwtService;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.processor.ValidationResponse;
import com.challenge.tteapp.repository.UserRepository;
import com.challenge.tteapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ValidationResponse validationResponse;
    private final PasswordEncoder passwordEncoder;
    private final ValidationError validationError;
        @Override
        public ResponseEntity<Object> loginUser(LogInOutUser logInOutUser, String requestId) {
            try {
                log.info("Login user , requestId: [{}]", requestId);
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInOutUser.getEmail(), logInOutUser.getPassword()));
                User userAuth = userRepository.findByEmail(logInOutUser.getEmail())
                        .orElseThrow();
                TokenRequest token = new TokenRequest();
                token.setToken(jwtService.getToken(userAuth));
                User user = userRepository.findElement(logInOutUser.getEmail());
                if (user.getState().equals(1)) {
                    log.warn("The user is already logged in, requestId: {}", requestId);
                    return new ResponseEntity<>(validationError.getStructureError(HttpStatus.BAD_REQUEST.value(),
                            "The user is already logged in"), HttpStatus.BAD_REQUEST);
                } else {
                    user.setState(1);
                    userRepository.save(user);
                }
                return new ResponseEntity<>(token, HttpStatus.CREATED);

            }catch (AuthenticationException e) {
                return new ResponseEntity<>(validationError.getStructureError(HttpStatus.BAD_REQUEST.value(),
                        "Incorrect email or password"), HttpStatus.BAD_REQUEST);
            }
        }
    @Override
    public ResponseEntity<Object> registerShopper(ShopperDTO shopperDTO, String requestId) {
        log.info("Save Shopper information in database, requestId: {}", requestId);
        if (userRepository.findElement(shopperDTO.getEmail()) != null) {
            return validationResponse.createDuplicateResponse("Email", requestId);
        }
        if (userRepository.findElement(shopperDTO.getUsername()) != null) {
            return validationResponse.createDuplicateResponse("Username", requestId);
        }
        User shopper = new User();
        shopper.setUsername(shopperDTO.getUsername());
        shopper.setEmail(shopperDTO.getEmail());
        shopper.setPassword(passwordEncoder.encode(shopperDTO.getPassword()));
        shopper.setRole("SHOPPER");
        shopper.setState(0);
        userRepository.save(shopper);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(jwtService.getToken(shopper));
        userResponse.setEmail(shopper.getEmail());
        userResponse.setUsername(shopper.getUsername());
        log.info("creation Shopper successful, with requestId: [{}]", requestId);
        return new ResponseEntity<>(userResponse,HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Object> logoutUser(LogInOutUser logInOutUser, String requestId) {
        try {
            log.info("Logout user , requestId: [{}]", requestId);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInOutUser.getEmail(), logInOutUser.getPassword()));
            User user = userRepository.findElement(logInOutUser.getEmail());
            if (user.getState().equals(1)) {
                user.setState(0);
                userRepository.save(user);
            } else {
                log.warn("The user is already logout, requestId: {}", requestId);
                return new ResponseEntity<>(validationError.getStructureError(HttpStatus.BAD_REQUEST.value(),
                        "The user is already logout"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new Status("ok"), HttpStatus.CREATED);

        }catch (AuthenticationException e) {
            return new ResponseEntity<>(validationError.getStructureError(HttpStatus.BAD_REQUEST.value(),
                    "Incorrect email or password"), HttpStatus.BAD_REQUEST);
        }
    }
}
