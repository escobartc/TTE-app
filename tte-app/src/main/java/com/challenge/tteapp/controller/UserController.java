package com.challenge.tteapp.controller;

import com.challenge.tteapp.configuration.InfoToken;
import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.dto.CartDTO;
import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.model.dto.UpdateStatusOrderDTO;
import com.challenge.tteapp.model.dto.WishListDTO;
import com.challenge.tteapp.model.response.*;
import com.challenge.tteapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${service.controller.path}")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponse> createShopper(@RequestBody @Valid ShopperDTO shopperDTO) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, creation Shopper, with requestId: [{}]", requestId);
        return userService.registerShopper(shopperDTO, requestId);
    }

    @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LogInUser user) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, login user, with requestId: [{}]", requestId);
        return userService.loginUser(user, requestId);
    }

    @PostMapping(path = "/logout", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StatusResponse> logoutUser(@RequestBody @Valid LogOutUser user) {
        String requestId = UUID.randomUUID().toString();
        String email = InfoToken.getName();
        log.info("JOIN TO TTE-APP, logout user, with requestId: [{}]", requestId);
        return userService.logoutUser(user,email, requestId);
    }

        @PostMapping(path = "/forgotPassword", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StatusResponse> forgotPassword(@RequestBody @Valid ForgotPass forgotPass) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, logout user, with requestId: [{}]", requestId);
        return userService.forgotPass(forgotPass, requestId);
    }

    @GetMapping(path = "/user/wishlist")
    public ResponseEntity<WishListResponse> retrieverList() {
        String email = InfoToken.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, retriever user list, with requestId: [{}]", requestId);
        return userService.retrieverList(email, requestId);
    }

    @PostMapping(path = "/user/wishlist/add/{product_id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StatusResponse> addElementList(@PathVariable("product_id") String idProduct, @RequestBody WishListDTO wishListDTO) {
        String email = InfoToken.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, retriever List, with requestId: [{}]", requestId);
        return userService.addElementList(wishListDTO, idProduct, email, requestId);
    }

    @DeleteMapping(path = "/user/wishlist/remove/{product_id}")
    public ResponseEntity<StatusResponse> removeListElement(@PathVariable("product_id") String idProduct) {
        String email = InfoToken.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, remove element in wishList, with requestId: [{}]", requestId);
        return userService.removeListElement(email, idProduct, requestId);
    }

    @PostMapping(path = "/cart/add", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MessageResponse> cartList(@RequestBody @Valid CartDTO cartDTO) {
        String email = InfoToken.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, cart user: {}, with requestId: [{}]", email, requestId);
        return userService.cartList(cartDTO, email, requestId);
    }

    @GetMapping(path = "/cart")
    public ResponseEntity<CartResponse> retrieverCart() {
        String email = InfoToken.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, cart retriever: {}, with requestId: [{}]", email, requestId);
        return userService.retrieverCart(email, requestId);
    }

    @PostMapping(path = "/cart", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CartBeforeCheckResponse> addCoupon(@RequestBody CouponCode couponCode) {
        String email = InfoToken.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, cart user: {}, with requestId: [{}]", email, requestId);
        return userService.addCoupon(couponCode, email, requestId);
    }

    @GetMapping(path = "/cart/checkout")
    public ResponseEntity<MessageResponse> cartCheckout() {
        String email = InfoToken.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, cart checkout: {}, with requestId: [{}]", email, requestId);
        return userService.cartCheckout(email, requestId);
    }

    @GetMapping(path = "/cart/checkout/review")
    public ResponseEntity<List<CartCheckoutResponse>> cartCheckoutReview() {
        String email = InfoToken.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, cart checkout review: {}, with requestId: [{}]", email, requestId);
        return userService.cartCheckoutReview(email, null,requestId);
    }
    @GetMapping(path = "/cart/checkout/review/{userId}")
    public ResponseEntity<List<CartCheckoutResponse>> cartCheckoutReview(@PathVariable Long userId) {
        String email = InfoToken.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, cart checkout review whit user id: {}, with requestId: [{}]", email, requestId);
        return userService.cartCheckoutReview(email, userId,requestId);
    }


    @PutMapping(path = "/cart/checkout/review")
    public ResponseEntity<MessageResponse> cartCheckoutUpdateState(@RequestBody UpdateStatusOrderDTO updateStatusOrderDTO) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, updateOrder , with requestId: [{}]", requestId);
        return userService.cartCheckoutUpdateState(updateStatusOrderDTO, requestId);
    }

}
