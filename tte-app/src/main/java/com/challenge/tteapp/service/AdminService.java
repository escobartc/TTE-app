package com.challenge.tteapp.service;

import com.challenge.tteapp.model.User;
import com.challenge.tteapp.model.ViewUsers;
import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.admin.LoginAdmin;
import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.model.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<Object> register(UserDTO userDTO, String requestId);
    ResponseEntity<Object> registerAdmin(Admin admin, String requestId);
    ResponseEntity<Object> loginAdmin(LoginAdmin admin, String requestId);
    ResponseEntity<ViewUsers> viewUsers(String requestId);
    ResponseEntity<Object> userUpdate(UserDTO userDTOUpdate, String requestId);


}
