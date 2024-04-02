package com.challenge.tteapp;

import com.challenge.tteapp.controller.ProductController;
import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.Review;
import com.challenge.tteapp.model.User;
import com.challenge.tteapp.model.dto.ReviewDTO;
import com.challenge.tteapp.model.response.MessageResponse;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.repository.ReviewRepository;
import com.challenge.tteapp.repository.UserRepository;
import com.challenge.tteapp.service.ProductService;
import com.challenge.tteapp.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductController productController;
    @InjectMocks
    private ProductServiceImpl productServiceimpl;
    @Mock
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void getProductReviewsTest(){
        ResponseEntity<List<ReviewDTO>> successResponse = new ResponseEntity<>(new ArrayList<>(), HttpStatus.CREATED);
        when(productService.getProductReviews(eq(1L), anyString())).thenReturn(successResponse);
        ResponseEntity<List<ReviewDTO>> response = productController.getProductReviews(1L);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Optional<Product> product1 = buildProduct();
        assertThrows(HttpClientErrorException.class, () -> {
            productServiceimpl.getProductReviews(1L, "requestId");
        });
        when(productRepository.findById(anyLong())).thenReturn(product1);
        ResponseEntity<List<ReviewDTO>> response2 = productServiceimpl.getProductReviews(1L, "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }

    private static Optional<Product> buildProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setState("STATE");
        Review review = new Review();
        review.setComment("comments");
        List<Review> reviews = new ArrayList<>();
        reviews.add(review);
        product.setReviews(reviews);
        Optional<Product> product1 = Optional.of(product);
        return product1;
    }

    @Test
    void addProductReviewTest(){
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setProductId(1L);
        reviewDTO.setUser("user");
        reviewDTO.setComment("comment");
        assertThrows(HttpClientErrorException.class, () -> {
            productServiceimpl.addProductReview(1L,reviewDTO, "requestId","email");
        });
        Optional<Product> product1 = buildProduct();
        when(productRepository.findById(anyLong())).thenReturn(product1);
        Review review = new Review();
        review.setId(1L);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        assertThrows(HttpClientErrorException.class, () -> {
            productServiceimpl.addProductReview(1L,reviewDTO, "requestId","email");
        });
        User user = new User();
        user.setUsername("name");
        User user22 = new User();
        user.setUsername("nameW");
        Optional<User> user1 = Optional.of(user);
        Optional<User> user2 = Optional.of(user22);
        when(userRepository.findByUsername(anyString())).thenReturn(user1);
        when(userRepository.findByEmail(anyString())).thenReturn(user2);
        assertThrows(HttpClientErrorException.class, () -> {
            productServiceimpl.addProductReview(1L,reviewDTO, "requestId","email");
        });
        when(userRepository.findByEmail(anyString())).thenReturn(user1);
        ResponseEntity<MessageResponse> response2 = productServiceimpl.addProductReview(1L,reviewDTO, "requestId","email");
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
    }

}
