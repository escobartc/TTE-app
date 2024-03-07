package com.challenge.tteapp.controller;

import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.admin.LoginAdmin;
import com.challenge.tteapp.model.dto.UserDTO;
import com.challenge.tteapp.service.AuthService;
import com.challenge.tteapp.service.LegacyAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${service.controller.path}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AdminController {

    private final AuthService authService;
    private final LegacyAdmin adminService;

    @PostMapping(path= "/admin/create", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createAdmin(@RequestBody Admin admin) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP with requestId: [{}]", requestId);
        return authService.registerAdmin(admin, requestId);
    }
    @PostMapping(path= "/admin/login", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> loginAdmin(@RequestBody LoginAdmin admin) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP with requestId: [{}]", requestId);
        return authService.loginAdmin(admin, requestId);
    }

    @PostMapping(path= "/admin/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP with requestId: {}", requestId);
        return authService.register(userDTO, requestId);
    }

    @PostMapping(path = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createProduct(@RequestBody Product product) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP with requestId: {}", requestId);
        ResponseEntity<Object> response = adminService.saveProduct(product, requestId);
        Long productId = null;
        if (response.getStatusCode() == HttpStatus.CREATED) {
            productId = ((Product) Objects.requireNonNull(response.getBody())).getId();
        }
        Map<String, Object> responseBody = new HashMap<>();
        if (response.getStatusCode() == HttpStatus.CREATED) {
            responseBody.put("productId", productId);
            responseBody.put("message", "Product created successfully");
        } else {
            responseBody.put("message", "Failed to create product");
        }
        return ResponseEntity.status(response.getStatusCode()).body(responseBody);
    }

}
