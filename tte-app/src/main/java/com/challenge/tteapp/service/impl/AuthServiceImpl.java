package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.User;
import com.challenge.tteapp.model.UserResponse;
import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.admin.LoginAdmin;
import com.challenge.tteapp.model.TokenRequest;
import com.challenge.tteapp.model.dto.UserDTO;
import com.challenge.tteapp.processor.ValidationResponse;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.repository.AdminRepository;
import com.challenge.tteapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AdminRepository adminRepository;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ValidationResponse validationResponse;


    public ResponseEntity<Object> register(UserDTO userDTO, String requestId) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return validationInfo(user, requestId);
    }
    public ResponseEntity<Object> registerAdmin(Admin admin, String requestId) {
        User user = new User();
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        BeanUtils.copyProperties(admin, user);
        user.setRole("ADMIN");
        return validationInfo(user, requestId);
    }
    public ResponseEntity<Object> loginAdmin(LoginAdmin admin, String requestId) {
        log.info("Login Admin , requestId: [{}]", requestId);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword()));
        UserDetails user = adminRepository.findByUsername(admin.getUsername()).orElseThrow();
        TokenRequest token = new TokenRequest();
        token.setToken(jwtService.getToken(user));
        return new ResponseEntity<>(token,HttpStatus.CREATED);
    }

    private ResponseEntity<Object> validationInfo(User user, String requestId){
        log.info("Save information in database, requestId: {}", requestId);
        if (adminRepository.findElement(user.getEmail()) != null) {
            return validationResponse.createDuplicateResponse("Email", requestId);
        }
        if (adminRepository.findElement(user.getUsername()) != null) {
            return validationResponse.createDuplicateResponse("Username", requestId);
        }
        adminRepository.save(user);
        return new ResponseEntity<>(createUserResponse(user), HttpStatus.CREATED);
    }
    private UserResponse createUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(jwtService.getToken(user));
        if (user.getRole().equals("ADMIN")) {
            return userResponse;
        } else {
            userResponse.setEmail(user.getEmail());
            userResponse.setUsername(user.getUsername());
            userResponse.setRole(user.getRole());
            return userResponse;
        }
    }

}
