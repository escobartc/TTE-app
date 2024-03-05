package com.endava.tteapp.controller;

import com.endava.tteapp.LoggerPrinter;
import com.endava.tteapp.model.DTO.UserDTO;
import com.endava.tteapp.model.Product;
import com.endava.tteapp.model.Shopper;
import com.endava.tteapp.model.User;
import com.endava.tteapp.service.LegacyAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.endava.tteapp.model.constant.constant.NAME;
import static com.endava.tteapp.model.constant.constant.WEB;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {

    private final LegacyAdmin adminService;
    @PostMapping(path= "/admin/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO) {
        LoggerPrinter loggerPrinter = new LoggerPrinter(NAME, UUID.randomUUID().toString(), WEB, "");
        loggerPrinter.log(LogLevel.INFO, "JOIN TO TTE-APP");
        return adminService.saveUser(userDTO, loggerPrinter);
    }

    @PostMapping("/auth")
    public Shopper createShopper(@RequestBody Shopper shopper) {
        return shopper;
    }

    @PostMapping(path = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createProduct(@RequestBody Product product) {
        // Create logger printer
        LoggerPrinter loggerPrinter = new LoggerPrinter(NAME, UUID.randomUUID().toString(), WEB, "");
        loggerPrinter.log(LogLevel.INFO, "Creating a new product");

        // Create product in the database
        ResponseEntity<Object> response = adminService.saveProduct(product, loggerPrinter); //  include role

        // Extract product ID from the response body
        Long productId = null;
        if (response.getStatusCode() == HttpStatus.CREATED) {
            productId = ((Product) Objects.requireNonNull(response.getBody())).getId();
        }

        // Construct the response based on success/failure
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
