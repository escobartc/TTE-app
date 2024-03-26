package com.challenge.tteapp.service;

import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.admin.LoginAdmin;
import com.challenge.tteapp.model.dto.CouponDTO;
import com.challenge.tteapp.model.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<UserResponse> register(UserDTO userDTO, String requestId);
    ResponseEntity<UserResponse> registerAdmin(Admin admin, String requestId);
    ResponseEntity<TokenRequest> loginAdmin(LoginAdmin admin, String requestId);
    ResponseEntity<UsersList> viewUsers(String requestId);
    ResponseEntity<Object> userUpdate(UserDTO userDTOUpdate, String requestId);
    ResponseEntity<Object> deleteUser(UsersDTO users, String requestId);
    ResponseEntity<StatusResponse> createCoupon(CouponDTO couponDTO,String email, String requestId);
    ResponseEntity<List<Coupon>> viewAllCoupon(String requestId);
    ResponseEntity<StatusResponse> deleteCoupon(CouponDelete couponDelete, String requestId);




}
