package com.challenge.tteapp.service;

import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.model.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface ShopperService {
    ResponseEntity<Object> registerShopper(ShopperDTO shopperDTO, String requestId);


}
