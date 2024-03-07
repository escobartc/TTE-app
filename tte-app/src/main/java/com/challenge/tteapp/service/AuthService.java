package com.challenge.tteapp.service;

import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.admin.LoginAdmin;
import com.challenge.tteapp.model.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<Object> register(UserDTO userDTO, String requestId);
    ResponseEntity<Object> registerAdmin(Admin admin, String requestId);
    ResponseEntity<Object> loginAdmin(LoginAdmin admin, String requestId);

}
