package com.challenge.tteapp;

import com.challenge.tteapp.controller.AdminController;
import com.challenge.tteapp.controller.UserController;
import com.challenge.tteapp.model.LogInOutUser;
import com.challenge.tteapp.model.User;
import com.challenge.tteapp.model.UserResponse;
import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.processor.JwtService;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.processor.ValidationResponse;
import com.challenge.tteapp.repository.UserRepository;
import com.challenge.tteapp.service.UserService;
import com.challenge.tteapp.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    @InjectMocks
    private UserServiceImpl userServiceimpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private ValidationResponse validationResponse;
    @Mock
    private ValidationError validationError;
    @Test
    public void CreateShopperTest() {
        ShopperDTO shopperDTO = ShopperInfo();
        UserResponse userResponse = new UserResponse();
        ResponseEntity<Object> successResponse = new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        when(userService.registerShopper(eq(shopperDTO), anyString())).thenReturn(successResponse);
        ResponseEntity<Object> response = userController.createShopper(shopperDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ResponseEntity<Object> response2 = userServiceimpl.registerShopper(shopperDTO, "requestId");

        assertEquals(HttpStatus.CREATED, response2.getStatusCode());

    }
    @Test
    public void LoginUserTest() {
        LogInOutUser logInOutUser = LoginOutUser();
        UserResponse userResponse = new UserResponse();
        ResponseEntity<Object> successResponse = new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        when(userService.loginUser(eq(logInOutUser), anyString())).thenReturn(successResponse);
        ResponseEntity<Object> response = userController.loginUser(logInOutUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User existingUser = userInfo();
        existingUser.setState(1);
        when(userRepository.findElement(anyString())).thenReturn(existingUser);
        ResponseEntity<Object> response2 = userServiceimpl.loginUser(logInOutUser, "requestId");
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());

    }

    @Test
    public void LogOut() {
        LogInOutUser logInOutUser = LoginOutUser();
        UserResponse userResponse = new UserResponse();
        ResponseEntity<Object> successResponse = new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        when(userService.logoutUser(eq(logInOutUser), anyString())).thenReturn(successResponse);
        ResponseEntity<Object> response = userController.logoutUser(logInOutUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User existingUser = userInfo();
        existingUser.setState(1);
        when(userRepository.findElement(anyString())).thenReturn(existingUser);
        ResponseEntity<Object> response2 = userServiceimpl.logoutUser(logInOutUser, "requestId");
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());

    }

    @Test
    public void LogOut2() {
        LogInOutUser logInOutUser = LoginOutUser();
        UserResponse userResponse = new UserResponse();
        ResponseEntity<Object> successResponse = new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        when(userService.logoutUser(eq(logInOutUser), anyString())).thenReturn(successResponse);
        ResponseEntity<Object> response = userController.logoutUser(logInOutUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User existingUser = userInfo();
        existingUser.setState(0);
        when(userRepository.findElement(anyString())).thenReturn(existingUser);
        ResponseEntity<Object> response2 = userServiceimpl.logoutUser(logInOutUser, "requestId");
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());

    }
    @Test
    public void LoginUserTest2() {
        LogInOutUser logInOutUser = LoginOutUser();
        UserResponse userResponse = new UserResponse();
        ResponseEntity<Object> successResponse = new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        when(userService.loginUser(eq(logInOutUser), anyString())).thenReturn(successResponse);
        ResponseEntity<Object> response = userController.loginUser(logInOutUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User existingUser = userInfo();
        existingUser.setState(0);
        when(userRepository.findElement(anyString())).thenReturn(existingUser);
        ResponseEntity<Object> response2 = userServiceimpl.loginUser(logInOutUser, "requestId");
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());

    }

    private static User userInfo() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existingUser");
        existingUser.setEmail("existing@example.com");
        return existingUser;
    }

    @Test
    public void UserValidationEmail() {
        ShopperDTO shopperDTO = ShopperInfo();
        when(userRepository.findElement(eq(shopperDTO.getEmail()))).thenReturn(new User());
        ResponseEntity<Object> response2 = userServiceimpl.registerShopper(shopperDTO, "requestId");

        verify(validationResponse).createDuplicateResponse(eq("Email"), eq("requestId"));
    }
    @Test
    public void UserValidationUsername() {
        ShopperDTO shopperDTO = ShopperInfo();
        when(userRepository.findElement(eq(shopperDTO.getUsername()))).thenReturn(new User());
        ResponseEntity<Object> response2 = userServiceimpl.registerShopper(shopperDTO, "requestId");
        verify(validationResponse).createDuplicateResponse(eq("Username"), eq("requestId"));
    }

    private static ShopperDTO ShopperInfo(){
        ShopperDTO shopperDTO = new  ShopperDTO();
        shopperDTO.setEmail("email");
        shopperDTO.setUsername("username");
        shopperDTO.setPassword("password");
        return shopperDTO;
    }

    private static LogInOutUser LoginOutUser(){
        LogInOutUser LoginOutUser = new  LogInOutUser();
        LoginOutUser.setEmail("email");
        LoginOutUser.setPassword("password");
        return LoginOutUser;
    }

}
