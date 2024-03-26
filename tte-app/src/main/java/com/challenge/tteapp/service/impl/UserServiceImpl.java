package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.dto.CartDTO;
import com.challenge.tteapp.model.dto.CouponDTO;
import com.challenge.tteapp.model.dto.ShopperDTO;
import com.challenge.tteapp.model.dto.WishListDTO;
import com.challenge.tteapp.processor.JwtService;
import com.challenge.tteapp.processor.ValidationResponse;
import com.challenge.tteapp.repository.*;
import com.challenge.tteapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Map;
import java.util.Optional;

import static com.challenge.tteapp.model.Constants.MESSAGE;

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
    @Override
    public ResponseEntity<LoginResponse> loginUser(LogInOutUser logInOutUser, String requestId) {
        log.info("Login user, requestId: [{}]", requestId);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInOutUser.getEmail(), logInOutUser.getPassword()));
            User userAuth = userRepository.findElement(logInOutUser.getEmail());
            String name = userAuth.getUsername();
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
            return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Incorrect email or password") {};
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
        User shopper = builShopper(shopperDTO);
        userRepository.save(shopper);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(jwtService.getToken(shopper));
        userResponse.setEmail(shopper.getEmail());
        userResponse.setUsername(shopper.getUsername());
        log.info("creation Shopper successful, with requestId: [{}]", requestId);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    private User builShopper(ShopperDTO shopperDTO) {
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
            return new ResponseEntity<>(new StatusResponse("ok"), HttpStatus.CREATED);
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
        return new ResponseEntity<>(wishListResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<StatusResponse> addElementList(WishListDTO wishListDTO, String idProduct, String email, String requestId) {
        log.info("Add product in wishlist, requestId: [{}]", requestId);
        User user;
        if(wishListDTO.getUser_id().isEmpty()) {
             user = userRepository.findElement(email);
        }else {
             user = userRepository.findElement(wishListDTO.getUser_id());
        }
        if(user==null) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User don't exist");
        List<Integer> idProducts = productRepository.findProductById();
        if (idProducts.contains(Integer.parseInt(idProduct))) {
            List<Integer> wishList = wishListRepository.findArticleIdsByUserId(user.getId());
            if (wishList.isEmpty() || !wishList.contains(Integer.parseInt(idProduct))) {
                wishListRepository.addElementToList(user.getId(), Integer.parseInt(idProduct));
                log.info("Element successfully added, requestId: [{}]", requestId);
                return new ResponseEntity<>(new StatusResponse("Element successfully added"), HttpStatus.CREATED);
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
            return new ResponseEntity<>(new StatusResponse("Elements successful remove"), HttpStatus.CREATED);
        } else {
            log.warn("The element does not exist in wishlist, requestId: [{}]", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The element does not exist in wishlist");
        }
    }

    @Override
    public ResponseEntity<Object> cartList(CartDTO cartDTO, String email, String requestId) {
        log.info("Add product in cart, requestId: [{}]", requestId);
        User user = userRepository.findElement(email);

        List<Integer> idProducts = productRepository.findProductById();
        List<Integer> carList = cartRepository.findArticleIdsByUserId(user.getId());

        Integer productId = cartDTO.getProduct_id();
        if (carList.contains(productId)) {
            log.warn("Product already exists in the cart: [{}]", productId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The product is already in the cart");
        }

        Integer quantity = productRepository.availableProducts(Long.valueOf(productId));
        if (cartDTO.getQuantity() > quantity) {
            log.warn("Not enough quantity available for product: [{}]", productId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Not enough quantity available for product");
        }

        if (quantity > 0 && cartDTO.getQuantity() > 0) {
            cartRepository.addElementToList(user.getId(), productId, cartDTO.getQuantity());
            log.info("Product added successfully, requestId: [{}]", requestId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(MESSAGE, "Product added successfully"));
        } else {
            log.warn("Not possible to add to cart: [{}]", productId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Not possible to add to cart");
        }
    }

    @Override
    public ResponseEntity<Object> retrieverCart(String email, String requestId) {
        log.info("Search user in database , requestId: [{}]", requestId);
        User user = userRepository.findElement(email);
        List<Products> productsList = constructionListProducts(user);
        CartResponse cartResponse = new CartResponse();
        cartResponse.setUser_id(user.getId().toString());
        cartResponse.setProducts(productsList);
        log.info("Retriever cart list successful, requestId: [{}]", requestId);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> retrieverCartCheckout(String email, String requestId) {
        User user = userRepository.findElement(email);
        List<Long> cartResponse = cartRepository.AllCart(user.getId());
        Optional<Coupon> list = Optional.empty();

        if(!cartResponse.isEmpty()){
            list = couponRepository.findById(cartResponse.get(0));
        }

        CartBeforeCheck cartBeforeCheck = new CartBeforeCheck();
        cartBeforeCheck.setUser_id(user.getId().toString());
        List<Products> productsList = constructionListProducts(user);
        cartBeforeCheck.setShopping_cart(productsList);

        CouponDTO couponApplied = new CouponDTO();
        couponApplied.setDiscountPercentage(list.get().getDiscountPercentage());
        couponApplied.setCouponCode(list.get().getCouponCode());
        cartBeforeCheck.setCoupon_applied(couponApplied);


        log.info("Retriever cart list successful, requestId: [{}]", requestId);
        return new ResponseEntity<>(cartBeforeCheck, HttpStatus.OK);    }

    private List<Products> constructionListProducts(User user) {
        List<Object[]> productsData = cartRepository.findProductsCartById(user.getId());
        List<Products> productsList = new ArrayList<>();
        for (Object[] row : productsData) {
            Products product = new Products();
            product.setProduct_cart((int) row[0]);
            product.setQuantity((String) row[1]);
            productsList.add(product);
        }
        return productsList;
    }

    @Override
    public ResponseEntity<Object> addCoupon(CouponCode couponCode, String email, String requestId) {
        log.info("add coupon in cart: {} , requestId: [{}]", requestId,email);
        User user = userRepository.findElement(email);
        Coupon coupon = couponRepository.findCoupon(couponCode.getCoupon_code());

        if(coupon == null){
            log.warn("coupon dont exist, please verify your information, requestId: [{}]", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "coupon dont exist, please verify your information");
        }else{
            cartRepository.updateCartCoupon(coupon.getId(),user.getId());
            log.info("Coupon successful added: {} , requestId: [{}]", requestId,email);
            return new ResponseEntity<>(new StatusResponse("Coupon successful added"), HttpStatus.CREATED);
        }
    }
}
