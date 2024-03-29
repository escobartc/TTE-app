package com.challenge.tteapp;

import com.challenge.tteapp.configuration.UserRoleContext;
import com.challenge.tteapp.controller.UserController;
import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.model.dto.WishListDTO;
import com.challenge.tteapp.processor.JwtService;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.processor.ValidationResponse;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.repository.UserRepository;
import com.challenge.tteapp.repository.WishListRepository;
import com.challenge.tteapp.service.UserService;
import com.challenge.tteapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    @InjectMocks
    private UserServiceImpl userServiceimpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WishListRepository wishListRepository;
    @Mock
    private ProductRepository productRepository;
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
    void CreateShopperTest() {
        ShopperDTO shopperDTO = ShopperInfo();
        UserResponse userResponse = new UserResponse();
        ResponseEntity<UserResponse> successResponse = new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        when(userService.registerShopper(eq(shopperDTO), anyString())).thenReturn(successResponse);
        ResponseEntity<UserResponse> response = userController.createShopper(shopperDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ResponseEntity<UserResponse> response2 = userServiceimpl.registerShopper(shopperDTO, "requestId");

        assertEquals(HttpStatus.CREATED, response2.getStatusCode());

    }

    @Test
    void LoginUserTest() {
        LogInOutUser logInOutUser = LoginOutUser();
        ResponseEntity<LoginResponse> errorResponse = new ResponseEntity<>(new LoginResponse(), HttpStatus.BAD_REQUEST);
        when(userService.loginUser(eq(logInOutUser), anyString())).thenReturn(errorResponse);
        ResponseEntity<LoginResponse> response = userController.loginUser(logInOutUser);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        User existingUser = userInfo();
        existingUser.setState(1);
        when(userRepository.findElement(anyString())).thenReturn(existingUser);
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.loginUser(logInOutUser, "requestId");
        });
    }

    @Test
    void LogOut() {
        LogInOutUser logInOutUser = LoginOutUser();
        ResponseEntity<StatusResponse> successResponse = new ResponseEntity<>(new StatusResponse(), HttpStatus.CREATED);
        when(userService.logoutUser(eq(logInOutUser), anyString())).thenReturn(successResponse);
        ResponseEntity<StatusResponse> response = userController.logoutUser(logInOutUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User existingUser = userInfo();
        existingUser.setState(1);
        when(userRepository.findElement(anyString())).thenReturn(existingUser);
        ResponseEntity<StatusResponse> response2 = userServiceimpl.logoutUser(logInOutUser, "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());

    }

    @Test
    void LogOut2() {
        LogInOutUser logInOutUser = LoginOutUser();
        ResponseEntity<StatusResponse> successResponse = new ResponseEntity<>(new StatusResponse(), HttpStatus.CREATED);
        when(userService.logoutUser(eq(logInOutUser), anyString())).thenReturn(successResponse);
        ResponseEntity<StatusResponse> response = userController.logoutUser(logInOutUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User existingUser = userInfo();
        existingUser.setState(0);
        when(userRepository.findElement(anyString())).thenReturn(existingUser);
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.logoutUser(logInOutUser, "requestId");
        });
    }

    @Test
    void LoginUserTest2() {
        LogInOutUser logInOutUser = LoginOutUser();
        LoginResponse loginResponse = new LoginResponse();
        ResponseEntity<LoginResponse> successResponse = new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
        when(userService.loginUser(eq(logInOutUser), anyString())).thenReturn(successResponse);
        ResponseEntity<LoginResponse> response = userController.loginUser(logInOutUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User existingUser = userInfo();
        existingUser.setState(0);
        when(userRepository.findElement(anyString())).thenReturn(existingUser);
        ResponseEntity<LoginResponse> response2 = userServiceimpl.loginUser(logInOutUser, "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());

    }

    @Test
    void retrieverList() {
        WishListResponse wishListResponse = new WishListResponse();
        wishListResponse.setUser_id("id");

        ResponseEntity<WishListResponse> successResponse = new ResponseEntity<>(wishListResponse, HttpStatus.CREATED);
        lenient().when(userService.retrieverList(eq(wishListResponse.getUser_id()), anyString())).thenReturn(successResponse);
        ResponseEntity<WishListResponse> response = userController.retrieverList();
        User user = new User();
        user.setId(1L);
        when(userRepository.findElement(anyString())).thenReturn(user);

        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        values.add(3);
        lenient().when(wishListRepository.findArticleIdsByUserId(anyLong())).thenReturn(values);

        ResponseEntity<WishListResponse> response2 = userServiceimpl.retrieverList("email", "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }

    @Test
    void retrieverListError() {
        WishListResponse wishListResponse = new WishListResponse();
        wishListResponse.setUser_id("id");

        ResponseEntity<WishListResponse> successResponse = new ResponseEntity<>(wishListResponse, HttpStatus.CREATED);
        lenient().when(userService.retrieverList(eq(wishListResponse.getUser_id()), anyString())).thenReturn(successResponse);
        ResponseEntity<WishListResponse> response = userController.retrieverList();
        User user = new User();
        user.setId(1L);
        when(userRepository.findElement(anyString())).thenReturn(user);
        lenient().when(wishListRepository.findArticleIdsByUserId(anyLong())).thenReturn(new ArrayList<>());

        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.removeListElement("email", "1", "requestId");
        });
    }
    @Test
    void addElementListTest() {
        WishListDTO wishListDTO = new WishListDTO();
        wishListDTO.setUserId("1");
        WishListResponse wishListResponse = new WishListResponse();
        wishListResponse.setUser_id("id");

        ResponseEntity<StatusResponse> successResponse = new ResponseEntity<>(new StatusResponse(), HttpStatus.CREATED);
        lenient().when(userService.addElementList(eq(wishListDTO), anyString() ,anyString() ,anyString())).thenReturn(successResponse);
        ResponseEntity<StatusResponse> response = userController.addElementList("idProduct",wishListDTO);

        User user = new User();
        user.setId(1L);
        when(userRepository.findId(anyLong())).thenReturn(user);
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.addElementList(wishListDTO, "1", "email","requestId");
        });
    }

    @Test
    void addElementListTesError() {
        WishListDTO wishListDTO = new WishListDTO();
        wishListDTO.setUserId("12");
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.addElementList(wishListDTO, "1", "email","requestId");
        });
    }

    @Test
    void removeListElementTest() {
        ResponseEntity<StatusResponse> successResponse = new ResponseEntity<>(new StatusResponse(), HttpStatus.CREATED);
        lenient().when(userService.removeListElement(eq("idProduct"), eq("email"), anyString())).thenReturn(successResponse);
        ResponseEntity<StatusResponse> response = userController.removeListElement("idProduct");
        User user = new User();
        user.setId(1L);
        when(userRepository.findElement(anyString())).thenReturn(user);

        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        values.add(3);
        lenient().when(wishListRepository.findArticleIdsByUserId(anyLong())).thenReturn(values);

        ResponseEntity<StatusResponse> response2 = userServiceimpl.removeListElement("email", "1", "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());

    }

    private static User userInfo() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existingUser");
        existingUser.setEmail("existing@example.com");
        return existingUser;
    }

    @Test
    void UserValidationEmail() {
        ShopperDTO shopperDTO = ShopperInfo();
        when(userRepository.findElement(eq(shopperDTO.getEmail()))).thenReturn(new User());
        ResponseEntity<UserResponse> response2 = userServiceimpl.registerShopper(shopperDTO, "requestId");

        verify(validationResponse).createDuplicateResponse(eq("Email"), eq("requestId"));
    }

    @Test
    void UserValidationUsername() {
        ShopperDTO shopperDTO = ShopperInfo();
        lenient().when(userRepository.findElement(eq(shopperDTO.getUsername()))).thenReturn(new User());
        ResponseEntity<UserResponse> response2 = userServiceimpl.registerShopper(shopperDTO, "requestId");
        verify(validationResponse).createDuplicateResponse(eq("Username"), eq("requestId"));
    }

    private static ShopperDTO ShopperInfo() {
        ShopperDTO shopperDTO = new ShopperDTO();
        shopperDTO.setEmail("email");
        shopperDTO.setUsername("username");
        shopperDTO.setPassword("password");
        return shopperDTO;
    }

    private static LogInOutUser LoginOutUser() {
        LogInOutUser LoginOutUser = new LogInOutUser();
        LoginOutUser.setEmail("email");
        LoginOutUser.setPassword("password");
        return LoginOutUser;
    }

}
