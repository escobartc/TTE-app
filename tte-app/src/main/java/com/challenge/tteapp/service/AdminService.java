package com.challenge.tteapp.service;

import com.challenge.tteapp.model.UsersList;
import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.admin.LoginAdmin;
import com.challenge.tteapp.model.dto.UserDTO;
import com.challenge.tteapp.model.UsersDTO;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<Object> register(UserDTO userDTO, String requestId);
    ResponseEntity<Object> registerAdmin(Admin admin, String requestId);
    ResponseEntity<Object> loginAdmin(LoginAdmin admin, String requestId);
    ResponseEntity<UsersList> viewUsers(String requestId);
    ResponseEntity<Object> userUpdate(UserDTO userDTOUpdate, String requestId);
    ResponseEntity<Object> deleteUser(UsersDTO users, String requestId);
}
