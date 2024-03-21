package com.challenge.tteapp.controller;

import com.challenge.tteapp.configuration.UserRoleContext;
import com.challenge.tteapp.model.LogInOutUser;
import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.model.dto.WishListDTO;
import com.challenge.tteapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("${service.controller.path}")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    @PostMapping(path= "/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createShopper(@RequestBody ShopperDTO shopperDTO) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, creation Shopper, with requestId: [{}]", requestId);
        return userService.registerShopper(shopperDTO, requestId);
    }
    @PostMapping(path= "/login", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> loginUser(@RequestBody LogInOutUser user) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, login user, with requestId: [{}]", requestId);
        return userService.loginUser(user, requestId);
    }
    @PostMapping(path= "/logout", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> logoutUser(@RequestBody LogInOutUser user) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, logout user, with requestId: [{}]", requestId);
        return userService.logoutUser(user, requestId);
    }

    @GetMapping(path= "/user/wishlist")
    public ResponseEntity<Object> retrieverList() {
        String email = UserRoleContext.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, retriever List, with requestId: [{}]", requestId);
        return userService.retrieverList(email, requestId);
    }

    @PostMapping(path= "/user/wishlist/add/{product_id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> AddElementList(@PathVariable("product_id") String idProduct, @RequestBody WishListDTO wishListDTO) {
        String email = UserRoleContext.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, retriever List, with requestId: [{}]", requestId);
        return userService.addElementList(wishListDTO, idProduct,email, requestId);
    }

    @DeleteMapping(path= "/user/wishlist/remove/{product_id}")
    public ResponseEntity<Object> removeListElement(@PathVariable("product_id") String idProduct) {
        String email = UserRoleContext.getName();
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, remove element in wishList, with requestId: [{}]", requestId);
        return userService.removeListElement(email,idProduct, requestId);
    }


}
