package com.challenge.tteapp.controller;

import com.challenge.tteapp.configuration.UserRoleContext;
import com.challenge.tteapp.model.CouponDelete;
import com.challenge.tteapp.model.UsersList;
import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.admin.LoginAdmin;
import com.challenge.tteapp.model.dto.CouponDTO;
import com.challenge.tteapp.model.dto.UserDTO;
import com.challenge.tteapp.model.UsersDTO;
import com.challenge.tteapp.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("${service.controller.path}")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @PostMapping(path= "/admin/create", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createAdmin(@RequestBody Admin admin) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, creation admin, with requestId: [{}]", requestId);
        return adminService.registerAdmin(admin, requestId);
    }
    @PostMapping(path= "/admin/login", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> loginAdmin(@RequestBody LoginAdmin admin) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, login admin, with requestId: [{}]", requestId);
        return adminService.loginAdmin(admin, requestId);
    }

    @PostMapping(path= "/admin/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, creation user by admin, with requestId: [{}]", requestId);
        return adminService.register(userDTO, requestId);
    }

    @GetMapping(path= "/user")
    public ResponseEntity<UsersList> viewUser() {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, view all user by admin, with requestId: {}", requestId);
        return adminService.viewUsers(requestId);
    }
    @PutMapping(path= "/user")
    public ResponseEntity<Object> updatingUser(@RequestBody UserDTO userDTOUpdate) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, Update user by admin, with requestId: {}", requestId);
        return adminService.userUpdate(userDTOUpdate, requestId);
    }
    @DeleteMapping(path= "/user")
    public ResponseEntity<Object> deleteUser(@RequestBody UsersDTO users) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, delete user by admin, with requestId: {}", requestId);
        return adminService.deleteUser(users, requestId);
    }
    @PostMapping(path= "/coupon", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createCoupon(@RequestBody @Valid CouponDTO couponDTO) {
        String requestId = UUID.randomUUID().toString();
        String email = UserRoleContext.getName();
        log.info("JOIN TO TTE-APP, creation coupon by admin, with requestId: [{}]", requestId);
        return adminService.createCoupon(couponDTO,email, requestId);
    }
    @GetMapping(path= "/coupon")
    public ResponseEntity<Object> viewAllCoupon() {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, creation user by admin, with requestId: [{}]", requestId);
        return adminService.viewAllCoupon(requestId);
    }
    @DeleteMapping(path= "/coupon", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> deleteCoupon(@RequestBody CouponDelete couponDelete) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, creation user by admin, with requestId: [{}]", requestId);
        return adminService.deleteCoupon(couponDelete, requestId);
    }

}
