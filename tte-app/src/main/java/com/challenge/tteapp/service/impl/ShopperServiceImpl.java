package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.Shopper;
import com.challenge.tteapp.model.User;
import com.challenge.tteapp.model.UserResponse;
import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.processor.ValidationResponse;
import com.challenge.tteapp.repository.ShopperRepository;
import com.challenge.tteapp.service.ShopperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShopperServiceImpl implements ShopperService {

    private final JwtServiceImpl jwtService;
    private final ShopperRepository shopperRepository;
    private final ValidationResponse validationResponse;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Object> registerShopper(ShopperDTO shopperDTO, String requestId) {
        log.info("Save Shopper information in database, requestId: {}", requestId);
        if (shopperRepository.findElement(shopperDTO.getEmail()) != null) {
            return validationResponse.createDuplicateResponse("Email", requestId);
        }
        if (shopperRepository.findElement(shopperDTO.getUsername()) != null) {
            return validationResponse.createDuplicateResponse("Username", requestId);
        }
        Shopper shopper = new Shopper();
        shopper.setUsername(shopperDTO.getUsername());
        shopper.setEmail(shopperDTO.getEmail());
        shopper.setPassword(passwordEncoder.encode(shopperDTO.getPassword()));
        shopper.setRole("SHOPPER");
        shopperRepository.save(shopper);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(jwtService.getToken(shopper));
        userResponse.setEmail(shopper.getEmail());
        userResponse.setUsername(shopper.getUsername());
        log.info("creation Shopper successful, with requestId: [{}]", requestId);
        return new ResponseEntity<>(userResponse,HttpStatus.CREATED);

    }
}
