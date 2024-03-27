package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.admin.Admin;
import com.challenge.tteapp.model.admin.LoginAdmin;
import com.challenge.tteapp.model.dto.ApprovalAdminDTO;
import com.challenge.tteapp.model.dto.CouponDTO;
import com.challenge.tteapp.model.dto.UserDTO;
import com.challenge.tteapp.processor.JwtService;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.processor.ValidationResponse;
import com.challenge.tteapp.repository.*;
import com.challenge.tteapp.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

import static com.challenge.tteapp.model.Constants.MESSAGE;

@AllArgsConstructor
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final WishListRepository wishListRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ValidationResponse validationResponse;

    @Override
    public ResponseEntity<UserResponse> register(UserDTO userDTO, String requestId) {
        log.info("creation user by admin, with requestId: [{}]", requestId);
        if (!userDTO.getRole().equalsIgnoreCase("employee")) {
            log.error("Role must be 'employee' for user registration, with requestId: [{}]", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Role must be 'employee' for user registration");
        }
        User user = buildUser(userDTO);
        return validationInfo(user, requestId);
    }
    @Override
    public ResponseEntity<UserResponse> registerAdmin(Admin admin, String requestId) {
        User user = new User();
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        BeanUtils.copyProperties(admin, user);
        user.setRole("ADMIN");
        user.setState(0);
        return validationInfo(user, requestId);
    }
    @Override
    public ResponseEntity<TokenRequest> loginAdmin(LoginAdmin admin, String requestId) {
        try {
            log.info("Login Admin , requestId: [{}]", requestId);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getEmail(), admin.getPassword()));
            User user = userRepository.findByEmail(admin.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("User not found with email: " + admin.getEmail()));
            TokenRequest token = new TokenRequest();
            token.setToken(jwtService.getToken(user));
            log.info("Token created: {} , requestId: [{}]", token.getToken(), requestId);
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            log.error("Incorrect email or password , requestId: [{}]", requestId);
            throw new AuthenticationException("Incorrect email or password") {
            };
        }
    }

    @Override
    public ResponseEntity<MessageResponse> approvalJobs(ApprovalAdminDTO approvalAdminDTO, String operation, String requestId) {
        log.info("change status for jobs by admin, with requestId: {}", requestId);
        String action = approvalAdminDTO.getAction();
        if (operation.equals("product")) {
            log.info("search product by admin, with requestId: {}", requestId);
            return handleProductApproval(approvalAdminDTO, action,requestId);
        } else if (operation.equals("category")) {
            log.info("search category by admin, with requestId: {}", requestId);
            return handleCategoryApproval(approvalAdminDTO, action,requestId);
        } else {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Type incorrect");
        }
    }

    private ResponseEntity<MessageResponse> handleProductApproval(ApprovalAdminDTO approvalAdminDTO, String action,String requestId) {
        Product product = productRepository.findProductId(approvalAdminDTO.getId());
        if (product != null) {
            if (action.equals("approve")) {
                log.info("approve product by admin, with requestId: {}", requestId);
                product.setState(approvalAdminDTO.getAction());
                productRepository.save(product);
                log.info("Success product Approval, with requestId: {}", requestId);
                return ResponseEntity.ok(new MessageResponse("Success Approval"));
            } else if (action.equals("decline")) {
                log.info("decline product by admin, with requestId: {}", requestId);
                productRepository.delete(product);
                log.info("Product deleted successfully, with requestId: {}", requestId);
                return ResponseEntity.ok(new MessageResponse("Product deleted successfully"));
            }
            log.error("Invalid action. Action must be 'approve' or 'decline', with requestId: {}", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid action. Action must be 'approve' or 'decline'");
        } else {
            log.error("Product ID incorrect, please verify your information, with requestId: {}", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Product ID incorrect, please verify your information");
        }
    }
    private ResponseEntity<MessageResponse> handleCategoryApproval(ApprovalAdminDTO approvalAdminDTO, String action, String requestId) {
        Category category = categoryRepository.findCategoryId(approvalAdminDTO.getId());
        if (category != null) {
            if (action.equals("approve")) {
                log.info("approve category by admin, with requestId: {}", requestId);
                category.setState(approvalAdminDTO.getAction());
                categoryRepository.save(category);
                log.info("Success category Approval, with requestId: {}", requestId);
                return ResponseEntity.ok(new MessageResponse("Success Approval"));
            } else if (action.equals("decline")) {
                log.info("decline category by admin, with requestId: {}", requestId);
                categoryRepository.delete(category);
                log.info("Category deleted successfully, with requestId: {}", requestId);
                return ResponseEntity.ok(new MessageResponse("Category deleted successfully"));
            }
            log.error("Invalid action. Action must be 'approve' or 'decline', with requestId: {}", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid action. Action must be 'approve' or 'decline'");
        } else {
            log.error("Category ID incorrect, please verify your information, with requestId: {}", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Category ID incorrect, please verify your information");
        }
    }


    @Override
    public ResponseEntity<ApprovalJobs> viewApprovalJobs(String requestId) {
        log.info("search status categories and products for approval jobs by admin, with requestId: {}", requestId);
        List<Category> categories = categoryRepository.findAllCategoryOperations();
        List<Product> products = productRepository.findAllProductsOperations();
        List<JobsResponse> jobs = new ArrayList<>();
        for (Category category : categories) {
            jobs.add(new JobsResponse("category", category.getId(), category.getState()));
        }
        for (Product product : products) {
            jobs.add(new JobsResponse("product", product.getId(), product.getState()));
        }
        log.info("return successful categories and products for approval jobs by admin, with requestId: {}", requestId);
        return new ResponseEntity<>(new ApprovalJobs(jobs),HttpStatus.OK);
    }
    @Override
    public ResponseEntity<UsersList> viewUsers(String requestId) {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream().map(this::mapToUserDTO).toList();
        UsersList usersList = new UsersList();
        usersList.setUsers(userDTOs);
        return ResponseEntity.ok(usersList);
    }

    @Override
    public ResponseEntity<MessageResponse> userUpdate(UserDTO userDTOUpdate, String requestId) {
        User user = userRepository.findElement(userDTOUpdate.getUsername());
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "data incorrect, please verify your information");
        }
        if (userDTOUpdate.getEmail() != null) {
            if (userRepository.findElement(userDTOUpdate.getEmail()) != null) {
                log.warn("email exist in database, with requestId: [{}]", requestId);
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "email exist in database");
            }
            user.setEmail(userDTOUpdate.getEmail());
        }
        if (userDTOUpdate.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTOUpdate.getPassword()));
        }
        userRepository.save(user);
        return new ResponseEntity<>(new MessageResponse("User " + userDTOUpdate.getUsername() + " has been updated successfully"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MessageResponse> deleteUser(UsersDTO users, String requestId) {
        List<String> deletedUsernames = new ArrayList<>();
        for (String username : users.getUsers()) {
            User user = userRepository.findElement(username);
            if (user != null) {
                List<Integer> wishList = wishListRepository.findArticleIdsByUserId(user.getId());
                if (!wishList.isEmpty()) {
                    wishListRepository.deleteById(user.getId());
                }
                userRepository.delete(user);
                deletedUsernames.add(username);
            }
        }
        if (!deletedUsernames.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("users deleted successfully"+ deletedUsernames),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("No users found for deletion."+ deletedUsernames),HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<StatusResponse> createCoupon(@RequestBody CouponDTO couponDTO, String email, String requestId) {
        log.info("creation coupon, requestId: [{}]", requestId);
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponDTO, coupon);
        coupon.setUseCoupon(Boolean.FALSE);
        List<String> coupons = couponRepository.findNameCoupon();
        if (!coupons.contains(couponDTO.getCouponCode())) {
            couponRepository.save(coupon);
            log.info("created successful, requestId: [{}]", requestId);
            return new ResponseEntity<>(new StatusResponse("created successful"), HttpStatus.CREATED);

        } else {
            log.error("The coupon exist in database, requestId: [{}]", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The coupon exist in database");
        }
    }

    @Override
    public ResponseEntity<List<Coupon>> viewAllCoupon(String requestId) {
        log.info("view All coupon, requestId: [{}]", requestId);
        return new ResponseEntity<>(couponRepository.findAll(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<StatusResponse> deleteCoupon(CouponDelete couponDelete, String requestId) {
        log.info("delete coupon, requestId: [{}]", requestId);
        Coupon coupon = couponRepository.findCoupon(couponDelete.getName());
        if (coupon != null) {
            couponRepository.deleteById(coupon.getId());
            log.error("delete coupon successful, requestId: [{}]", requestId);
            return new ResponseEntity<>(new StatusResponse("delete successful"), HttpStatus.CREATED);
        } else {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "coupon don't exist, please verify your information");
        }
    }

    private ResponseEntity<UserResponse> validationInfo(User user, String requestId) {
        log.info("Save user information in database, requestId: [{}]", requestId);
        if (userRepository.findElement(user.getEmail()) != null) {
            return validationResponse.createDuplicateResponse("Email", requestId);
        }
        if (userRepository.findElement(user.getUsername()) != null) {
            return validationResponse.createDuplicateResponse("Username", requestId);
        }
        userRepository.save(user);
        return new ResponseEntity<>(createUserResponse(user,requestId), HttpStatus.CREATED);
    }

    private UserResponse createUserResponse(User user, String requestId) {
        log.info("build UserResponse, requestId: [{}]", requestId);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(jwtService.getToken(user));
        if (!user.getRole().equals("ADMIN")) {
            userResponse.setEmail(user.getEmail());
            userResponse.setUsername(user.getUsername());
            userResponse.setRole(user.getRole());
        }
        return userResponse;
    }

    public UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private User buildUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setRole("EMPLOYEE");
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setState(0);
        return user;
    }
}
