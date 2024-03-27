package com.challenge.tteapp;

import com.challenge.tteapp.controller.AdminController;
import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.admin.LoginAdmin;
import com.challenge.tteapp.model.dto.CouponDTO;
import com.challenge.tteapp.model.dto.UserDTO;
import com.challenge.tteapp.processor.JwtService;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.processor.ValidationResponse;
import com.challenge.tteapp.repository.CouponRepository;
import com.challenge.tteapp.repository.UserRepository;
import com.challenge.tteapp.repository.WishListRepository;
import com.challenge.tteapp.service.AdminService;
import com.challenge.tteapp.service.ProductService;
import com.challenge.tteapp.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    @Mock
    private ProductService productService;
    @Mock
    private AdminService adminService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private WishListRepository wishListRepository;
    @InjectMocks
    private AdminController adminController;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AdminServiceImpl adminServiceImpl;
    @Mock
    private JwtService jwtService;
    @Mock
    private ValidationResponse validationResponse;
    @Mock
    private ValidationError validationError;

    @Test
    void RegisterAdminTest() {
        Admin admin = adminInfo();

        UserResponse userResponse = new UserResponse();
        ResponseEntity<UserResponse> successResponse = new ResponseEntity<>(userResponse, HttpStatus.CREATED);

        when(adminService.registerAdmin(eq(admin), anyString())).thenReturn(successResponse);

        ResponseEntity<UserResponse> response = adminController.createAdmin(admin);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        when(passwordEncoder.encode(admin.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<UserResponse> response2 = adminServiceImpl.registerAdmin(admin, "requestId");
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
    }

    @Test
    void RegisterUser() {
        UserDTO userDTO = userInfo();
        UserResponse userResponse = new UserResponse();
        ResponseEntity<UserResponse> successResponse = new ResponseEntity<>(userResponse, HttpStatus.CREATED);

        when(adminService.register(eq(userDTO), anyString())).thenReturn(successResponse);

        ResponseEntity<UserResponse> response = adminController.createUser(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<UserResponse> response2 = adminServiceImpl.register(userDTO, "requestId");
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        userDTO.setRole("other");
        assertThrows(HttpClientErrorException.class, () -> {
            adminServiceImpl.register(userDTO, "requestId");
        });
    }

    @Test
    void loginAdmin() {
        LoginAdmin loginAdmin = adminLoginInfo();
        TokenRequest tokenRequest = new TokenRequest();
        ResponseEntity<TokenRequest> successResponse = new ResponseEntity<>(tokenRequest, HttpStatus.CREATED);

        when(adminService.loginAdmin(eq(loginAdmin), anyString())).thenReturn(successResponse);

        ResponseEntity<TokenRequest> response = adminController.loginAdmin(loginAdmin);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        when(userRepository.findByEmail(loginAdmin.getEmail())).thenReturn(Optional.of(new User()));
        ResponseEntity<TokenRequest> response2 = adminServiceImpl.loginAdmin(loginAdmin, "requestId");

        assertEquals(HttpStatus.CREATED, response2.getStatusCode());

    }

    @Test
    void viewUserTest() {
        UsersList userResponse = new UsersList();
        ResponseEntity<UsersList> successResponse = new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        when(adminService.viewUsers(anyString())).thenReturn(successResponse);
        ResponseEntity<UsersList> response = adminController.viewUser();
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        UserRepository userRepositoryMock = mock(UserRepository.class);
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        ResponseEntity<UsersList> response2 = adminServiceImpl.viewUsers("requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        User user = new User();
        user.setState(1);
        user.setEmail("email");
        user.setRole("role");
        user.setUsername("username");
        adminServiceImpl.mapToUserDTO(user);
    }

    @Test
    void UserUpdate() {
        UserDTO userResponse = userInfo();
        ResponseEntity<MessageResponse> successResponse = new ResponseEntity<>(new MessageResponse(), HttpStatus.CREATED);
        when(adminService.userUpdate(eq(userResponse), anyString())).thenReturn(successResponse);
        ResponseEntity<MessageResponse> response = adminController.updatingUser(userResponse);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        when(userRepository.findElement(userResponse.getUsername())).thenReturn(new User());
        ResponseEntity<MessageResponse> response2 = adminServiceImpl.userUpdate(userResponse, "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());

        when(userRepository.findElement(userResponse.getUsername())).thenReturn(null);
        assertThrows(HttpClientErrorException.class, () -> {
            adminServiceImpl.userUpdate(userResponse, "requestId");
        });

    }

    @Test
    void deleteUser() {
        UsersDTO userResponse = new UsersDTO();
        List<String> users = new ArrayList<>();
        users.add("employee");
        users.add("other");
        userResponse.setUsers(users);

        ResponseEntity<Object> successResponse = new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        when(adminService.deleteUser(eq(userResponse), anyString())).thenReturn(successResponse);
        ResponseEntity<Object> response = adminController.deleteUser(userResponse);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());


        when(userRepository.findElement(userResponse.getUsers().get(0))).thenReturn(new User());
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        lenient().when(wishListRepository.findArticleIdsByUserId(anyLong())).thenReturn(list);
        ResponseEntity<Object> response2 = adminServiceImpl.deleteUser(userResponse, "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());

        when(userRepository.findElement(userResponse.getUsers().get(0))).thenReturn(null);
        ResponseEntity<Object> response3 = adminServiceImpl.deleteUser(userResponse, "requestId");
        assertEquals(HttpStatus.NOT_FOUND, response3.getStatusCode());

    }

    @Test
    void AdminValidationEmail() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        Admin admin = adminInfo();
        when(userRepository.findElement(eq(admin.getEmail()))).thenReturn(new User());
        ResponseEntity<UserResponse> response2 = adminServiceImpl.registerAdmin(admin, "requestId");

        verify(validationResponse).createDuplicateResponse(eq("Email"), eq("requestId"));
    }

    @Test
    void AdminValidationUsername() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        Admin admin = adminInfo();
        lenient().when(userRepository.findElement(eq(admin.getUsername()))).thenReturn(new User());
        ResponseEntity<UserResponse> response2 = adminServiceImpl.registerAdmin(admin, "requestId");
        verify(validationResponse).createDuplicateResponse(eq("Username"), eq("requestId"));
    }

    @Test
    void createCoupon() {
        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setCouponCode("CuouponTest");
        couponDTO.setDiscountPercentage(21);

        ResponseEntity<StatusResponse> successResponse = new ResponseEntity<>(new StatusResponse(), HttpStatus.CREATED);
        lenient().when(adminService.createCoupon(eq(couponDTO), anyString(),anyString())).thenReturn(successResponse);
        adminController.createCoupon(couponDTO);
        lenient().when(couponRepository.findNameCoupon()).thenReturn(new ArrayList<>());
        adminServiceImpl.createCoupon(couponDTO,"email" ,"requestId");
        List<String> coupons = new ArrayList<>();
        coupons.add("CuouponTest");
        lenient().when(couponRepository.findNameCoupon()).thenReturn(coupons);
        assertThrows(HttpClientErrorException.class, () -> {
            adminServiceImpl.createCoupon(couponDTO,"email", "requestId");
        });
    }

    @Test
    void viewCoupons(){
        ResponseEntity<List<Coupon>> successResponse = new ResponseEntity<>(new ArrayList<>(), HttpStatus.CREATED);
        lenient().when(adminService.viewAllCoupon(anyString())).thenReturn(successResponse);
        adminController.viewAllCoupon();
        lenient().when(couponRepository.findAll()).thenReturn(new ArrayList<>());
        adminServiceImpl.viewAllCoupon("requestId");
    }

    @Test
    void deleteCoupons(){
        CouponDelete couponDelete = new CouponDelete();
        couponDelete.setName("name");
        ResponseEntity<StatusResponse> successResponse = new ResponseEntity<>(new StatusResponse(), HttpStatus.CREATED);
        lenient().when(adminService.deleteCoupon(eq(couponDelete), anyString())).thenReturn(successResponse);
        adminController.deleteCoupon(couponDelete);
        lenient().when(couponRepository.findCoupon(anyString())).thenReturn(new Coupon());
        adminServiceImpl.deleteCoupon(couponDelete,"requestId");
    }

    @Test
    void deleteCouponError(){
        CouponDelete couponDelete = new CouponDelete();
        couponDelete.setName("name");
        assertThrows(HttpClientErrorException.class, () -> {
            adminServiceImpl.deleteCoupon(couponDelete, "requestId");
        });
    }

    private static Admin adminInfo() {
        Admin admin = new Admin();
        admin.setPassword("1234");
        admin.setUsername("username");
        admin.setEmail("email");
        return admin;
    }

    private static LoginAdmin adminLoginInfo() {
        LoginAdmin admin = new LoginAdmin();
        admin.setPassword("1234");
        admin.setEmail("email");
        return admin;
    }

    private static UserDTO userInfo() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("1234");
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setRole("employee");
        return userDTO;
    }


}
