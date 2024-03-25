package com.challenge.tteapp.controller;

import com.challenge.tteapp.configuration.UserRoleContext;
import com.challenge.tteapp.model.CouponCode;
import com.challenge.tteapp.model.LogInOutUser;
import com.challenge.tteapp.model.dto.CartDTO;
import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.model.dto.WishListDTO;
import com.challenge.tteapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${service.controller.path}")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createShopper(@RequestBody ShopperDTO shopperDTO) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, creation Shopper, with requestId: [{}]", requestId);
        return userService.registerShopper(shopperDTO, requestId);
    }

    @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> loginUser(@RequestBody LogInOutUser user) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, login user, with requestId: [{}]", requestId);
        return userService.loginUser(user, requestId);
    }

    @PostMapping(path = "/logout", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> logoutUser(@RequestBody LogInOutUser user) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, logout user, with requestId: [{}]", requestId);
        return userService.logoutUser(user, requestId);
    }

    @GetMapping(path = "/user/wishlist")
    public ResponseEntity<Object> retrieverList() {
        String email = UserRoleContext.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, retriever List, with requestId: [{}]", requestId);
        return userService.retrieverList(email, requestId);
    }

    @PostMapping(path = "/user/wishlist/add/{product_id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> addElementList(@PathVariable("product_id") String idProduct, @RequestBody WishListDTO wishListDTO) {
        String email = UserRoleContext.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, retriever List, with requestId: [{}]", requestId);
        return userService.addElementList(wishListDTO, idProduct, email, requestId);
    }

    @DeleteMapping(path = "/user/wishlist/remove/{product_id}")
    public ResponseEntity<Object> removeListElement(@PathVariable("product_id") String idProduct) {
        String email = UserRoleContext.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, remove element in wishList, with requestId: [{}]", requestId);
        return userService.removeListElement(email, idProduct, requestId);
    }

    @PostMapping(path = "/cart/add", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> cartList(@RequestBody CartDTO cartDTO) {
        String email = UserRoleContext.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, cart user: {}, with requestId: [{}]", email, requestId);
        return userService.cartList(cartDTO, email, requestId);
    }

    @GetMapping(path = "/cart")
    public ResponseEntity<Object> retrieverCart() {
        String email = UserRoleContext.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, cart retriever: {}, with requestId: [{}]", email, requestId);
        return userService.retrieverCart(email, requestId);
    }

    @PostMapping(path = "/cart", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> addCoupon(@RequestBody CouponCode couponCode) {
        String email = UserRoleContext.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, cart user: {}, with requestId: [{}]", email, requestId);
        return userService.addCoupon(couponCode, email, requestId);
    }

    @GetMapping(path = "/cart/checkout")
    public ResponseEntity<Object> retrieverCartCheckout() {
        String email = UserRoleContext.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, cart retriever checkout: {}, with requestId: [{}]", email, requestId);
        return userService.retrieverCartCheckout(email, requestId);
    }

}
