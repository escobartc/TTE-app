package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.TokenRequest;
import com.challenge.tteapp.model.User;
import com.challenge.tteapp.model.UserResponse;
import com.challenge.tteapp.model.ViewUsers;
import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.admin.LoginAdmin;
import com.challenge.tteapp.model.dto.UserDTO;
import com.challenge.tteapp.processor.JwtService;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.processor.ValidationResponse;
import com.challenge.tteapp.repository.UserRepository;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.swing.text.View;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    final UserRepository userRepository;
    final ProductRepository productRepository;
    final ValidationError validationError;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ValidationResponse validationResponse;


    public ResponseEntity<Object> register(UserDTO userDTO, String requestId) {
        if (!userDTO.getRole().equalsIgnoreCase("employee")) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Role must be 'employee' for user registration");
        }
        User user = new User();
        user.setRole("EMPLOYEE");
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setState(0);
        return validationInfo(user, requestId);
    }

    public ResponseEntity<Object> registerAdmin(Admin admin, String requestId) {
        User user = new User();
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        BeanUtils.copyProperties(admin, user);
        user.setRole("ADMIN");
        user.setState(0);
        return validationInfo(user, requestId);
    }

    public ResponseEntity<Object> loginAdmin(LoginAdmin admin, String requestId) {
        try {
            log.info("Login Admin , requestId: [{}]", requestId);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getEmail(), admin.getPassword()));
            User user = userRepository.findByEmail(admin.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("User not found with email: " + admin.getEmail()));
            TokenRequest token = new TokenRequest();
            token.setToken(jwtService.getToken(user));
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Incorrect email or password");
        }
    }

    @Override
    public ResponseEntity<ViewUsers> viewUsers(String requestId) {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream().map(this::mapToUserDTO).collect(Collectors.toList());
        ViewUsers viewUsers = new ViewUsers();
        viewUsers.setUsers(userDTOs);
        return ResponseEntity.ok(viewUsers);
    }

    @Override
    public ResponseEntity<Object> userUpdate(UserDTO userDTOUpdate, String requestId) {
        User user = userRepository.findElement(userDTOUpdate.getUsername());
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "data incorrect, please verify your information");
        }
        if (userDTOUpdate.getEmail() != null) {
            user.setEmail(userDTOUpdate.getEmail());
        }
        if (userDTOUpdate.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTOUpdate.getPassword()));
        }
        userRepository.save(user);
        return ResponseEntity.ok("User updated successfully");
    }

    private ResponseEntity<Object> validationInfo(User user, String requestId) {
        log.info("Save information in database, requestId: [{}]", requestId);
        if (userRepository.findElement(user.getEmail()) != null) {
            return validationResponse.createDuplicateResponse("Email", requestId);
        }
        if (userRepository.findElement(user.getUsername()) != null) {
            return validationResponse.createDuplicateResponse("Username", requestId);
        }
        userRepository.save(user);
        return new ResponseEntity<>(createUserResponse(user), HttpStatus.CREATED);
    }

    private UserResponse createUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(jwtService.getToken(user));
        if (!user.getRole().equals("ADMIN")) {
            userResponse.setEmail(user.getEmail());
            userResponse.setUsername(user.getUsername());
            userResponse.setRole(user.getRole());
        }
        return userResponse;
    }

    private UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

}

