package com.challenge.tteapp;

import com.challenge.tteapp.controller.UserController;
import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.dto.*;
import com.challenge.tteapp.model.response.*;
import com.challenge.tteapp.processor.JwtService;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.processor.ValidationResponse;
import com.challenge.tteapp.repository.*;
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
import org.springframework.web.client.HttpClientErrorException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private CartRepository cartRepository;
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrderProductsRepository orderProductsRepository;
    @Mock
    private CouponRepository couponRepository;
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
        LogInUser logInUser = LoginOutUser();
        ResponseEntity<LoginResponse> errorResponse = new ResponseEntity<>(new LoginResponse(), HttpStatus.BAD_REQUEST);
        when(userService.loginUser(eq(logInUser), anyString())).thenReturn(errorResponse);
        ResponseEntity<LoginResponse> response = userController.loginUser(logInUser);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        User existingUser = userInfo();
        existingUser.setState(1);
        when(userRepository.findElement(anyString())).thenReturn(existingUser);
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.loginUser(logInUser, "requestId");
        });
    }

    @Test
    void LoginUserTest2() {
        LogInUser logInUser = LoginOutUser();
        LoginResponse loginResponse = new LoginResponse();
        ResponseEntity<LoginResponse> successResponse = new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
        when(userService.loginUser(eq(logInUser), anyString())).thenReturn(successResponse);
        ResponseEntity<LoginResponse> response = userController.loginUser(logInUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User existingUser = userInfo();
        existingUser.setState(0);
        when(userRepository.findElement(anyString())).thenReturn(existingUser);
        ResponseEntity<LoginResponse> response2 = userServiceimpl.loginUser(logInUser, "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());

    }

    @Test
    void cartListTest() {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setProductId(1);
        cartDTO.setQuantity(1);
        MessageResponse messageResponse = new MessageResponse();
        ResponseEntity<MessageResponse> successResponse = new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
        lenient().when(userService.cartList(eq(cartDTO),anyString(), anyString())).thenReturn(successResponse);
        userController.cartList(cartDTO);
        User existingUser = userInfo();
        existingUser.setState(0);
        when(userRepository.findElement(anyString())).thenReturn(existingUser);
        when(cartRepository.findArticleIdsByUserId(anyLong())).thenReturn(new ArrayList<>());
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.cartList(cartDTO,"email", "requestId");
        });
        when(productRepository.availableProducts(anyLong())).thenReturn(2);
        ResponseEntity<MessageResponse> response2 = userServiceimpl.cartList(cartDTO,"email", "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        List<Integer> values = new ArrayList<>();
        values.add(1);
        when(cartRepository.findArticleIdsByUserId(anyLong())).thenReturn(values);
        ResponseEntity<MessageResponse> response3 = userServiceimpl.cartList(cartDTO,"email", "requestId");
        assertEquals(HttpStatus.OK, response3.getStatusCode());

    }

    @Test
    void retrieverCartTest() {
        User user = userInfo();
        List<Object[]> cartData = prepareCartData();
        CartResponse cartResponse = new CartResponse();
        ResponseEntity<CartResponse> successResponse = new ResponseEntity<>(cartResponse, HttpStatus.CREATED);
        lenient().when(userService.retrieverCart(anyString(), anyString())).thenReturn(successResponse);
        when(userRepository.findElement(anyString())).thenReturn(user);
        when(cartRepository.findProductsCartById(user.getId())).thenReturn(cartData);
        userController.retrieverCart();
        ResponseEntity<CartResponse> response2 = userServiceimpl.retrieverCart("email", "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }

    @Test
    void addCouponTest() {
        User user = userInfo();
        CouponCode couponCode = new CouponCode();
        couponCode.setCouponCod("");
        CartBeforeCheckResponse cartBeforeCheckResponse = new CartBeforeCheckResponse();
        ResponseEntity<CartBeforeCheckResponse> successResponse = new ResponseEntity<>(cartBeforeCheckResponse, HttpStatus.CREATED);
        lenient().when(userService.addCoupon(eq(couponCode),anyString(), anyString())).thenReturn(successResponse);
        userController.addCoupon(couponCode);
        when(userRepository.findElement(anyString())).thenReturn(user);
        when(ordersRepository.findOrders(anyLong(),anyString())).thenReturn(new Orders());
        ResponseEntity<CartBeforeCheckResponse> response2 = userServiceimpl.addCoupon(couponCode,"email", "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        couponCode.setCouponCod("coupon");
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.addCoupon(couponCode,"email", "requestId");
        });
        Coupon coupon = buildCoupon();
        List<Object[]> cartData = prepareCartData();
        when(cartRepository.findProductsCartById(user.getId())).thenReturn(cartData);
        when(couponRepository.findCoupon(anyString())).thenReturn(coupon);
        when(productRepository.findProductPriceById(anyLong())).thenReturn(2.0);
        ResponseEntity<CartBeforeCheckResponse> response3 = userServiceimpl.addCoupon(couponCode,"email", "requestId");
        assertEquals(HttpStatus.OK, response3.getStatusCode());
    }

    @Test
    void UserResponseTest(){
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail("email");
        userResponse.setUsername("username");
        userResponse.setRole("role");
        userResponse.setId("id");
        assertEquals("email",userResponse.getEmail());
        assertEquals("username",userResponse.getUsername());
        assertEquals("role",userResponse.getRole());
        assertEquals("id",userResponse.getId());
    }
    @Test
    void addCouponTest2() {
        User user = userInfo();
        CouponCode couponCode = new CouponCode();
        couponCode.setCouponCod("");
        CartBeforeCheckResponse cartBeforeCheckResponse = new CartBeforeCheckResponse();

        ResponseEntity<CartBeforeCheckResponse> successResponse = new ResponseEntity<>(cartBeforeCheckResponse, HttpStatus.CREATED);
        lenient().when(userService.addCoupon(eq(couponCode),anyString(), anyString())).thenReturn(successResponse);
        userController.addCoupon(couponCode);
        when(userRepository.findElement(anyString())).thenReturn(user);
        couponCode.setCouponCod("coupon");
        Coupon coupon = buildCoupon();
        List<Object[]> cartData = prepareCartData();
        when(cartRepository.findProductsCartById(user.getId())).thenReturn(cartData);
        when(couponRepository.findCoupon(anyString())).thenReturn(coupon);
        when(productRepository.findProductPriceById(anyLong())).thenReturn(2.0);
        ResponseEntity<CartBeforeCheckResponse> response3 = userServiceimpl.addCoupon(couponCode,"email", "requestId");
        assertEquals(HttpStatus.OK, response3.getStatusCode());
    }


    @Test
    void testCartBeforeCheckResponse() {
        CartBeforeCheckResponse response = new CartBeforeCheckResponse();
        response.setUserId("user123");
        List<Products> products = new ArrayList<>();
        response.setShoppingCart(products);
        CouponDTO coupon = new CouponDTO();
        coupon.setCouponCode("couponCode");
        coupon.setDiscountPercentage(50);
        response.setCouponApplied(coupon);
        response.setTotalBeforeDiscount(30.0);
        response.setTotalAfterDiscount(25.0);
        response.setShippingCost(5.0);
        response.setFinalTotal(30.0);

        assertEquals("user123", response.getUserId());
        assertEquals(products, response.getShoppingCart());
        assertEquals(coupon, response.getCouponApplied());
        assertEquals(30.0, response.getTotalBeforeDiscount(), 0.001);
        assertEquals(25.0, response.getTotalAfterDiscount(), 0.001);
        assertEquals(5.0, response.getShippingCost(), 0.001);
        assertEquals(30.0, response.getFinalTotal(), 0.001);
    }

    @Test
    void cartCheckoutTest() {
        User user = userInfo();
        CouponCode couponCode = new CouponCode();
        couponCode.setCouponCod("");
        MessageResponse messageResponse = new MessageResponse();
        ResponseEntity<MessageResponse> successResponse = new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
        lenient().when(userService.cartCheckout(anyString(), anyString())).thenReturn(successResponse);
        userController.cartCheckout();
        when(userRepository.findElement(anyString())).thenReturn(user);
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.cartCheckout("email", "requestId");
        });
        Orders orders = new Orders();
        orders.setOrderStatus("CREATED");
        orders.setUser(1L);
        orders.setId(1L);
        orders.setCouponId(1L);
        when(ordersRepository.findOrders(anyLong(),anyString())).thenReturn(orders);
        List<Object[]> cartData = prepareCartData();
        when(cartRepository.findProductsCartById(user.getId())).thenReturn(cartData);
        Product product = new Product();
        product.setInventory(new Inventory(1L,1,1));
        Optional<Product> product1 = Optional.of(product);
        when(productRepository.findById(anyLong())).thenReturn(product1);
        ResponseEntity<MessageResponse> response = userServiceimpl.cartCheckout("email", "requestId");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void cartCheckoutReviewTest(){
        ResponseEntity<List<CartCheckoutResponse>> successResponse = new ResponseEntity<>(new ArrayList<>(), HttpStatus.CREATED);
        lenient().when(userService.cartCheckoutReview(anyString(), anyLong(),anyString())).thenReturn(successResponse);
        userController.cartCheckoutReview();
        Orders orders = new Orders();
        orders.setUser(1L);
        orders.setId(1L);
        orders.setOrderStatus("orderStatus");
        orders.setCouponId(1L);
        List<Orders> orders1 = new ArrayList<>();
        orders1.add(orders);
        OrderProducts orderProducts = new OrderProducts();
        orderProducts.setOrderId(1L);
        orderProducts.setProductId(1L);
        orderProducts.setQuantity(1);
        orderProducts.setId(1L);
        List<OrderProducts> orderProducts1 = new ArrayList<>();
        orderProducts1.add(orderProducts);
        when(orderProductsRepository.findAllOrderProducts(anyLong())).thenReturn(orderProducts1);
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.cartCheckoutReview("email",1L, "requestId");
        });
        when(ordersRepository.findOrdersUserId(anyLong())).thenReturn(orders1);
        ResponseEntity<List<CartCheckoutResponse>> response = userServiceimpl.cartCheckoutReview("email",1L, "requestId");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        when(ordersRepository.findAllOrders()).thenReturn(orders1);
        ResponseEntity<List<CartCheckoutResponse>> response2 = userServiceimpl.cartCheckoutReview("email",null, "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }

    @Test
    void cartCheckoutUpdateStateTest(){
        MessageResponse messageResponse = new MessageResponse();
        ResponseEntity<MessageResponse> successResponse = new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
        UpdateStatusOrderDTO updateStatusOrderDTO = new UpdateStatusOrderDTO();
        updateStatusOrderDTO.setStatus("SEND");
        updateStatusOrderDTO.setIdOrder(1L);
        lenient().when(userService.cartCheckoutUpdateState(eq(updateStatusOrderDTO), anyString())).thenReturn(successResponse);
        userController.cartCheckoutUpdateState(updateStatusOrderDTO);
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.cartCheckoutUpdateState(updateStatusOrderDTO, "requestId");
        });
        when(ordersRepository.findOrdersId(1L)).thenReturn(new Orders());
        ResponseEntity<MessageResponse> response = userServiceimpl.cartCheckoutUpdateState(updateStatusOrderDTO, "requestId");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        updateStatusOrderDTO.setStatus("OTHER");
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.cartCheckoutUpdateState(updateStatusOrderDTO, "requestId");
        });

    }


    private static Coupon buildCoupon() {
        Coupon coupon = new Coupon();
        coupon.setUseCoupon(Boolean.FALSE);
        coupon.setCouponCode("coupon");
        coupon.setDiscountPercentage(12);
        coupon.setId(1L);
        return coupon;
    }


    @Test
    void retrieverList() {
        WishListResponse wishListResponse = new WishListResponse();
        wishListResponse.setUserId("id");

        ResponseEntity<WishListResponse> successResponse = new ResponseEntity<>(wishListResponse, HttpStatus.CREATED);
        lenient().when(userService.retrieverList(eq(wishListResponse.getUserId()), anyString())).thenReturn(successResponse);
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
        wishListResponse.setUserId("id");

        ResponseEntity<WishListResponse> successResponse = new ResponseEntity<>(wishListResponse, HttpStatus.CREATED);
        lenient().when(userService.retrieverList(eq(wishListResponse.getUserId()), anyString())).thenReturn(successResponse);
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
    void addElementListTest2() {
        WishListDTO wishListDTO = new WishListDTO();
        wishListDTO.setUserId("");
        User user = userInfo();
        when(userRepository.findElement(anyString())).thenReturn(user);
        List<Integer> idProducts = new ArrayList<>();
        idProducts.add(1);
        idProducts.add(2);
        when(productRepository.findProductById()).thenReturn(idProducts);

        when(wishListRepository.findArticleIdsByUserId(anyLong())).thenReturn(new ArrayList<>());
        ResponseEntity<StatusResponse> response2 = userServiceimpl.addElementList(wishListDTO,"1","email", "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        List<Integer> wishList = new ArrayList<>();
        wishList.add(1);
        when(wishListRepository.findArticleIdsByUserId(anyLong())).thenReturn(wishList);
        ExceptionAddElementList(wishListDTO);
        wishListDTO.setUserId("112");
        ExceptionAddElementList(wishListDTO);
        when(userRepository.findId(anyLong())).thenReturn(user);
        ExceptionAddElementList(wishListDTO);
        wishListDTO.setUserId("");
        idProducts.remove(0);
        ExceptionAddElementList(wishListDTO);
    }

    private void ExceptionAddElementList(WishListDTO wishListDTO) {
        assertThrows(HttpClientErrorException.class, () -> {
            userServiceimpl.addElementList(wishListDTO, "1", "email","requestId");
        });
    }

    @Test
    void removeListElementTest() {
        ResponseEntity<StatusResponse> successResponse = new ResponseEntity<>(new StatusResponse(), HttpStatus.CREATED);
        lenient().when(userService.removeListElement(eq("idProduct"), eq("email"), anyString())).thenReturn(successResponse);
        userController.removeListElement("idProduct");
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

    private static LogInUser LoginOutUser() {
        LogInUser loginUser = new LogInUser();
        loginUser.setEmail("email");
        loginUser.setPassword("password");
        return loginUser;
    }

    private List<Object[]> prepareCartData() {
        return Arrays.asList(new Object[]{1L, 2}, new Object[]{3L, 1});
    }

}
