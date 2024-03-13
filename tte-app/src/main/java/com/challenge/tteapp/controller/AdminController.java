package com.challenge.tteapp.controller;

import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.User;
import com.challenge.tteapp.model.ViewUsers;
import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.admin.LoginAdmin;
import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.model.dto.UserDTO;
import com.challenge.tteapp.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        log.info("JOIN TO TTE-APP, creation user by admin, with requestId: {}", requestId);
        return adminService.register(userDTO, requestId);
    }

    @GetMapping(path= "/user")
    public ResponseEntity<ViewUsers> viewUser() {
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

}
