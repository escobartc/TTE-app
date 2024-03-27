package com.challenge.tteapp.service;

import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.dto.CartDTO;
import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.model.dto.UpdateStatusOrderDTO;
import com.challenge.tteapp.model.dto.WishListDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<LoginResponse> loginUser(LogInOutUser logInOutUser, String requestId);
    ResponseEntity<StatusResponse> logoutUser(LogInOutUser logInOutUser, String requestId);
    ResponseEntity<UserResponse> registerShopper(ShopperDTO shopperDTO, String requestId);
    ResponseEntity<WishListResponse> retrieverList(String email,String requestId);
    ResponseEntity<StatusResponse> removeListElement(String email,String idProduct,String requestId);
    ResponseEntity<StatusResponse> addElementList(WishListDTO wishListDTO, String idProduct, String email,String requestId);
    ResponseEntity<MessageResponse> cartList(CartDTO cartDTO, String email, String requestId);
    ResponseEntity<CartResponse> retrieverCart(String email, String requestId);
    ResponseEntity<CartBeforeCheck> addCoupon(CouponCode couponCode, String email, String requestId);
    ResponseEntity<MessageResponse> cartCheckout(String email, String requestId);
    ResponseEntity<List<CartcheckoutReview>> cartCheckoutReview(String email, String requestId);
    ResponseEntity<Object> cartCheckoutUpdateState(UpdateStatusOrderDTO updateStatusOrderDTO, String requestId);








}
