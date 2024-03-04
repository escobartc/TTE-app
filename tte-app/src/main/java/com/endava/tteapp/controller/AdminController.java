package com.endava.tteapp.controller;

import com.endava.tteapp.LoggerPrinter;
import com.endava.tteapp.model.Shopper;
import com.endava.tteapp.model.User;
import com.endava.tteapp.service.LegacyAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.UUID;

import static com.endava.tteapp.model.constant.constant.NAME;
import static com.endava.tteapp.model.constant.constant.WEB;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {

    private final LegacyAdmin adminService;
    @PostMapping(path= "/admin/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        LoggerPrinter loggerPrinter = new LoggerPrinter(NAME, UUID.randomUUID().toString(), WEB, "");
        loggerPrinter.log(LogLevel.INFO, "JOIN TO TTE-APP");
        return adminService.saveUser(user, loggerPrinter);
    }
    @PostMapping("/auth")
    public Shopper createShopper(@RequestBody Shopper shopper){
        return shopper;
    }

}
