package com.challenge.tteapp.service;

import com.challenge.tteapp.model.CouponCode;
import com.challenge.tteapp.model.LogInOutUser;
import com.challenge.tteapp.model.dto.CartDTO;
import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.model.dto.WishListDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Object> loginUser(LogInOutUser logInOutUser, String requestId);
    ResponseEntity<Object> logoutUser(LogInOutUser logInOutUser, String requestId);
    ResponseEntity<Object> registerShopper(ShopperDTO shopperDTO, String requestId);
    ResponseEntity<Object> retrieverList(String email,String requestId);
    ResponseEntity<Object> removeListElement(String email,String idProduct,String requestId);
    ResponseEntity<Object> addElementList(WishListDTO wishListDTO, String idProduct, String email,String requestId);
    ResponseEntity<Object> cartList(CartDTO cartDTO, String email, String requestId);
    ResponseEntity<Object> retrieverCart(String email,String requestId);
    ResponseEntity<Object> retrieverCartCheckout(String email,String requestId);
    ResponseEntity<Object> addCoupon(CouponCode couponCode, String email, String requestId);





}
