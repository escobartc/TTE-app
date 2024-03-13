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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

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
        log.info("Login user, requestId: [{}]", requestId);
        try {
            User userAuth = userRepository.findElement(logInOutUser.getEmail());
            if (userAuth == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
            }
            String name = userAuth.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInOutUser.getEmail(), logInOutUser.getPassword()));
            if (userAuth.getState().equals(1)) {
                log.warn("The user is already logged in, requestId: {}", requestId);
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The user is already logged in");
            } else {
                userAuth.setState(1);
                userAuth.setUsername(name);
                userRepository.save(userAuth);
            }
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUsername(name);
            loginResponse.setEmail(logInOutUser.getEmail());
            loginResponse.setToken(jwtService.getToken(userAuth));
            return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Incorrect email or password");
        }
    }
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
        shopper.setRole("CUSTOMER");
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
            User user = userRepository.findElement(logInOutUser.getEmail());
            String name = user.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInOutUser.getEmail(), logInOutUser.getPassword()));
            if (user.getState().equals(1)) {
                user.setState(0);
                user.setUsername(name);
                userRepository.save(user);
            } else {
                log.warn("The user is already logout, requestId: {}", requestId);
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The user is already logout");
            }
            return new ResponseEntity<>(new Status("ok"), HttpStatus.CREATED);

        }catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect email or password", e);

        }
    }
}
