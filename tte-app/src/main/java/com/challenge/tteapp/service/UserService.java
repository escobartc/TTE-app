package com.challenge.tteapp.service;

import com.challenge.tteapp.model.LogInOutUser;
import com.challenge.tteapp.model.dto.ShopperDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Object> loginUser(LogInOutUser logInOutUser, String requestId);
    ResponseEntity<Object> logoutUser(LogInOutUser logInOutUser, String requestId);
    ResponseEntity<Object> registerShopper(ShopperDTO shopperDTO, String requestId);

}
