package com.challenge.tteapp.controller;

import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.service.ShopperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@RestController
@RequestMapping("${service.controller.path}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ShopperController {

    private final ShopperService shopperService;

    @PostMapping(path= "/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createAdmin(@RequestBody ShopperDTO shopperDTO) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, creation Shopper, with requestId: [{}]", requestId);
        return shopperService.registerShopper(shopperDTO, requestId);
    }

}
