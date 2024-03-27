package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.dto.*;
import com.challenge.tteapp.processor.JwtService;
import com.challenge.tteapp.processor.ValidationResponse;
import com.challenge.tteapp.repository.*;
import com.challenge.tteapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.challenge.tteapp.model.Constants.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ValidationResponse validationResponse;
    private final PasswordEncoder passwordEncoder;
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;
    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;

    @Override
    public ResponseEntity<LoginResponse> loginUser(LogInOutUser logInOutUser, String requestId) {
        log.info("Login user, requestId: [{}]", requestId);
        try {
            User userAuth = userRepository.findElement(logInOutUser.getEmail());
            if (userAuth == null) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The user does not exist");
            String name = userAuth.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInOutUser.getEmail(), logInOutUser.getPassword()));
            if (userAuth.getState().equals(1)) {
                log.warn("The user is already logged in, requestId: [{}]", requestId);
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The user is already logged in");
            } else {
                userAuth.setState(1);
                userAuth.setUsername(name);
                userRepository.save(userAuth);
            }
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUsername(name);
            loginResponse.setEmail(logInOutUser.getEmail());
            loginResponse.setToken(jwtService.getToken(userAuth));
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Incorrect email or password") {
            };
        }
    }

    @Override
    public ResponseEntity<UserResponse> registerShopper(ShopperDTO shopperDTO, String requestId) {
        log.info("Save Shopper information in database, requestId: [{}]", requestId);
        if (userRepository.findElement(shopperDTO.getEmail()) != null) {
            return validationResponse.createDuplicateResponse("Email", requestId);
        }
        if (userRepository.findElement(shopperDTO.getUsername()) != null) {
            return validationResponse.createDuplicateResponse("Username", requestId);
        }
        User shopper = buildShopper(shopperDTO);
        userRepository.save(shopper);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(jwtService.getToken(shopper));
        userResponse.setEmail(shopper.getEmail());
        userResponse.setUsername(shopper.getUsername());
        log.info("creation Shopper successful, with requestId: [{}]", requestId);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    private User buildShopper(ShopperDTO shopperDTO) {
        User shopper = new User();
        shopper.setUsername(shopperDTO.getUsername());
        shopper.setEmail(shopperDTO.getEmail());
        shopper.setPassword(passwordEncoder.encode(shopperDTO.getPassword()));
        shopper.setRole("CUSTOMER");
        shopper.setState(0);
        return shopper;
    }

    @Override
    public ResponseEntity<StatusResponse> logoutUser(LogInOutUser logInOutUser, String requestId) {
        try {
            log.info("Logout user , requestId: [{}]", requestId);
            User user = userRepository.findElement(logInOutUser.getEmail());
            String name = user.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInOutUser.getEmail(), logInOutUser.getPassword()));
            if (user.getState().equals(1)) {
                user.setState(0);
                user.setUsername(name);
                userRepository.save(user);
            } else {
                log.warn("The user is already logout, requestId: {}", requestId);
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The user is already logout");
            }
            log.info("Logout user: {} successful , requestId: [{}]", name, requestId);
            return new ResponseEntity<>(new StatusResponse("ok"), HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Incorrect email or password") {
            };
        }
    }

    @Override
    public ResponseEntity<WishListResponse> retrieverList(String email, String requestId) {
        User user = userRepository.findElement(email);
        List<Integer> wishList = wishListRepository.findArticleIdsByUserId(user.getId());
        WishListResponse wishListResponse = new WishListResponse();
        wishListResponse.setUser_id(user.getId().toString());
        wishListResponse.setWishlist(wishList);
        log.info("Retriever list successful, requestId: [{}]", requestId);
        return new ResponseEntity<>(wishListResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StatusResponse> addElementList(WishListDTO wishListDTO, String idProduct, String email, String requestId) {
        log.info("Add product in wishlist, requestId: [{}]", requestId);
        User user;
        if (wishListDTO.getUserId().isEmpty()) {
            user = userRepository.findElement(email);
        } else {
            user = userRepository.findId(Long.valueOf(wishListDTO.getUserId()));
        }

        if (user == null) {
            log.warn("User not found, requestId: [{}]", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User does not exist");
        }
        if (!wishListDTO.getUserId().isEmpty() && !email.equals(user.getEmail())) {
            log.warn("Mismatch between provided userId and email, requestId: [{}]", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Mismatch between provided userId and email");
        }
        List<Integer> idProducts = productRepository.findProductById();
        if (idProducts.contains(Integer.parseInt(idProduct))) {
            List<Integer> wishList = wishListRepository.findArticleIdsByUserId(user.getId());
            if (wishList.isEmpty() || !wishList.contains(Integer.parseInt(idProduct))) {
                wishListRepository.addElementToList(user.getId(), Integer.parseInt(idProduct));
                log.info("Element successfully added, requestId: [{}]", requestId);
                return new ResponseEntity<>(new StatusResponse("Element successfully added"), HttpStatus.OK);
            } else {
                log.warn("The element exists in the wishlist, requestId: [{}]", requestId);
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The element exists in the wishlist");
            }
        }
        log.warn("The product does not exist, requestId: [{}]", requestId);
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The product does not exist");
    }

    @Override
    public ResponseEntity<StatusResponse> removeListElement(String email, String idProduct, String requestId) {
        log.info("Remove product in wishlist , requestId: [{}]", requestId);
        User user = userRepository.findElement(email);
        List<Integer> wishList = wishListRepository.findArticleIdsByUserId(user.getId());
        if (wishList.contains(Integer.parseInt(idProduct))) {
            wishListRepository.deleteByUserIdAndArticleId(user.getId(), Integer.parseInt(idProduct));
            log.info("Elements successful remove , requestId: [{}]", requestId);
            return new ResponseEntity<>(new StatusResponse("Elements successful remove"), HttpStatus.OK);
        } else {
            log.warn("The element does not exist in wishlist, requestId: [{}]", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The element does not exist in wishlist");
        }
    }

    @Override
    public ResponseEntity<MessageResponse> cartList(CartDTO cartDTO, String email, String requestId) {
        log.info("Add product in cart, requestId: [{}]", requestId);
        User user = userRepository.findElement(email);
        List<Integer> cartList = cartRepository.findArticleIdsByUserId(user.getId());
        Integer productId = cartDTO.getProductId();
        Integer quantity = productRepository.availableProducts(Long.valueOf(productId));

        if (cartList.contains(productId) && quantity >= cartDTO.getQuantity()) {
            cartRepository.updateCartQuantity(cartDTO.getQuantity(), user.getId());
            return new ResponseEntity<>(new MessageResponse("Product quantity update successfully"), HttpStatus.OK);
        }
        if (cartDTO.getQuantity() > quantity) {
            log.warn("Not enough quantity available for product: [{}]", productId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Not enough quantity available for product" +
                    ", available: " + quantity);
        }
        cartRepository.addElementToList(user.getId(), productId, cartDTO.getQuantity());
        log.info("Product added successfully, requestId: [{}]", requestId);
        return new ResponseEntity<>(new MessageResponse("Product added successfully"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CartResponse> retrieverCart(String email, String requestId) {
        log.info("Search user in database , requestId: [{}]", requestId);
        User user = userRepository.findElement(email);
        List<Products> productsList = buildProducts(user);
        CartResponse cartResponse = new CartResponse();
        cartResponse.setUser_id(user.getId().toString());
        cartResponse.setProducts(productsList);
        log.info("Retriever cart list successful, requestId: [{}]", requestId);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    private List<Products> buildProducts(User user) {
        List<Object[]> cart = cartRepository.findProductsCartById(user.getId());
        List<Products> productsList = new ArrayList<>();
        for (Object[] element : cart) {
            Products products = new Products();
            products.setProductId((Long) element[0]);
            products.setQuantity((Integer) element[1]);
            productsList.add(products);
        }
        return productsList;
    }

    @Override
    public ResponseEntity<CartBeforeCheck> addCoupon(CouponCode couponCode, String email, String requestId) {
        log.info("Add coupon to cart: {} , requestId: [{}]", requestId, email);
        User user = userRepository.findElement(email);
        Coupon coupon = null;

        if (!couponCode.getCouponCode().isEmpty()) {
            coupon = couponRepository.findCoupon(couponCode.getCouponCode());
            if (coupon == null || coupon.getUseCoupon().equals(true)) {
                log.warn("Coupon does not exist or was used, please verify your information: {} , requestId: [{}]"
                        , requestId, email);
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Coupon does not exist or was used," +
                        " please verify your information");
            }
        }

        if (couponCode.getCouponCode().isEmpty()) {
            Long cartCouponId = ordersRepository.findCouponId(user.getId());
            if (cartCouponId != null) {
                couponRepository.updateCouponState(cartCouponId, Boolean.FALSE);
            }
        }

        buildOrder(Optional.ofNullable(coupon), user);
        log.info("Coupon successfully added: {} , requestId: [{}]", requestId, email);

        CartBeforeCheck cartBeforeCheck = buildResponseCheckout(user, Optional.ofNullable(coupon));
        return new ResponseEntity<>(cartBeforeCheck, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MessageResponse> cartCheckout(String email, String requestId) {
        log.info("cartCheckout cart: {} , requestId: [{}]", requestId, email);
        User user = userRepository.findElement(email);

        Orders orders = ordersRepository.findOrders(user.getId(), "CREATED");
        if(orders!=null && orders.getOrderStatus().equals(CREATED)){
            couponRepository.updateCouponState(orders.getCouponId(), Boolean.TRUE);
            List<Products> productsList = buildProducts(user);
            for (Products product : productsList) {
                OrderProducts orderProduct = new OrderProducts();
                orderProduct.setOrderId(orders.getId());
                orderProduct.setQuantity(product.getQuantity());
                orderProduct.setProductId(product.getProductId());
                orderProductsRepository.save(orderProduct);
            }
            orders.setOrderStatus("ACCEPTED");
            ordersRepository.save(orders);
        }else{
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "not orders found," +
                    " please verify your information");
        }
        cartRepository.deleteElementsByUserId(user.getId());
        return new ResponseEntity<>(new MessageResponse("thanks " +user.getUsername()+ " for your purchase"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CartcheckoutReview>> cartCheckoutReview(String email, String requestId) {
        log.info("cart checkout review: {} , requestId: [{}]", requestId, email);

        List<Orders> orders = ordersRepository.findAllOrders();
        List<CartcheckoutReview> response = new ArrayList<>();

        for (Orders order : orders) {
            List<OrderProducts> orderProducts = orderProductsRepository.findAllOrderProducts(order.getId());
            CartcheckoutReview cartcheckoutReview = new CartcheckoutReview();
            cartcheckoutReview.setUserId(order.getUser());
            cartcheckoutReview.setOrderId(order.getId());
            cartcheckoutReview.setStatus(order.getOrderStatus());

            List<Products> productsList = new ArrayList<>();
            for (OrderProducts orderProduct : orderProducts) {
                Products product = new Products();
                product.setProductId(orderProduct.getProductId());
                product.setQuantity(orderProduct.getQuantity());
                productsList.add(product);
            }
            cartcheckoutReview.setProducts(productsList);
            response.add(cartcheckoutReview);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> cartCheckoutUpdateState(UpdateStatusOrderDTO updateStatusOrderDTO, String requestId) {
        log.info("update order, requestId: [{}]", requestId);
        Orders order = ordersRepository.findOrdersId(updateStatusOrderDTO.getIdOrder());
        if (order != null) {
            String newStatus = updateStatusOrderDTO.getStatus();
            if ("SEND".equals(newStatus) || "CLOSED".equals(newStatus)) {
                order.setOrderStatus(newStatus);
                ordersRepository.save(order);
                return new ResponseEntity<>(new MessageResponse("Status update successful"), HttpStatus.OK);
            } else {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid status update request");
            }
        } else {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Order does not exist");
        }
    }



    private void buildOrder(Optional<Coupon> coupon, User user) {
        Long userId = user.getId();
        Orders existingOrder = ordersRepository.findOrders(userId, "CREATED");

        if (existingOrder != null) {
            existingOrder.setOrderStatus(CREATED);
            if (coupon.isPresent()) {
                existingOrder.setCouponId(coupon.get().getId());
            } else {
                existingOrder.setCouponId(null);
            }
            ordersRepository.save(existingOrder);
        } else {
            Orders newOrder = new Orders();
            newOrder.setUser(userId);
            newOrder.setOrderStatus(CREATED);
            if (coupon.isPresent()) {
                newOrder.setCouponId(coupon.get().getId());
            }
            ordersRepository.save(newOrder);
        }
    }

    private CartBeforeCheck buildResponseCheckout(User user, Optional<Coupon> coupon) {
        CartBeforeCheck cartBeforeCheck = new CartBeforeCheck();
        cartBeforeCheck.setUserId(user.getId().toString());

        List<Products> productsList = buildProducts(user);
        cartBeforeCheck.setShoppingCart(productsList);

        double totalBeforeDiscount = 0.0;

        for (Products product : productsList) {
            Double price = productRepository.findProductPriceById(product.getProductId());
            if (price != null) {
                totalBeforeDiscount += price * product.getQuantity();
            }
        }
        double totalAfterDiscount = totalBeforeDiscount;
        if (coupon.isPresent()) {
            double discountPercentage = coupon.get().getDiscountPercentage() / 100.0;
            totalAfterDiscount *= (1 - discountPercentage);
        }

        double finalTotal = totalAfterDiscount + 19.99;

        cartBeforeCheck.setTotalBeforeDiscount(totalBeforeDiscount);
        cartBeforeCheck.setTotalAfterDiscount(totalAfterDiscount);
        cartBeforeCheck.setShippingCost(19.99);
        cartBeforeCheck.setFinalTotal(finalTotal);

        CouponDTO couponApplied = new CouponDTO();
        couponApplied.setDiscountPercentage(coupon.map(Coupon::getDiscountPercentage).orElse(0));
        couponApplied.setCouponCode(coupon.map(Coupon::getCouponCode).orElse(null));
        cartBeforeCheck.setCouponApplied(couponApplied);
        return cartBeforeCheck;
    }

}
