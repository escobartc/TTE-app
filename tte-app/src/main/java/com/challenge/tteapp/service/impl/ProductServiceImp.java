package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.dto.CategoryDTO;
import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.model.dto.ReviewDTO;
import com.challenge.tteapp.repository.CategoryRepository;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.repository.ReviewRepository;
import com.challenge.tteapp.repository.UserRepository;
import com.challenge.tteapp.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Slf4j
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final String ERROR = "error";


    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductDTO)
                .toList();
    }

    private ProductDTO mapToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setTitle(product.getTitle());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategory(mapToCategoryDTO(product.getCategory())); // Map Category to CategoryDTO
        return productDTO;
    }

    private CategoryDTO mapToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public ResponseEntity<Object> saveProduct(ProductDTO productDTO, String requestId) {
        Product product = copyProductForDB(productDTO);

        // Resolve category based on name and set it in the product
        Category category = categoryRepository.findByName(productDTO.getCategory().getName());
        if (category == null) {
            // If category does not exist, create it
            category = new Category();
            category.setName(productDTO.getCategory().getName());
            category.setState(productDTO.getState());
            category = categoryRepository.save(category);
        }
        product.setCategory(category);

        // Save the Product entity
        Product savedProduct = productRepository.save(product);

        // Return ResponseEntity with the saved Product
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @Override
    public List<ReviewDTO> getProductReviews(Long productId) {
        return productRepository.findById(productId)
                .map(product -> product.getReviews().stream()
                        .map(review -> convertToReviewDTO(review, productId))
                        .toList()
                )
                .orElse(Collections.emptyList());
    }


    public ReviewDTO convertToReviewDTO(Review review,Long productId) {
        // Convert the Review object to a ReviewDTO object
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUser(review.getUser());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setProductId(productId);
        // Set other properties as needed
        return reviewDTO;
    }


    @Override
    public ResponseEntity<Object> addProductReview(Long productId, ReviewDTO reviewDTO, String requestId) {
        Review review = new Review();
        boolean validUser = doesUserExist(reviewDTO.getUser());
        boolean validProduct = doesProductExist(productId);
        boolean validComment = !reviewDTO.getComment().isEmpty();

        if (validUser && validProduct && validComment) {
            // Get the product from the database using its ID
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                // Set the product association in the review
                review.setProduct_id(product);
                review.setUser(reviewDTO.getUser());
                review.setComment(reviewDTO.getComment());
                Review savedReview = reviewRepository.save(review);
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("review_id", savedReview.getId());
                responseBody.put("message", "Successful");
                return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
            } else {
                // Handle the case where the product does not exist
                return ResponseEntity.badRequest().body("Invalid product ID: " + productId);
            }
        } else {
            // Handle invalid fields
            Map<String, String> response = new HashMap<>();
            if (!validUser) {
                response.put("userError", "User with provided username does not exist");
            }
            if (!validProduct) {
                response.put("productError", "Invalid product or product does not exist");
            }
            if (!validComment) {
                response.put("commentError", "Invalid comment, should not be empty");
            }
            return ResponseEntity.badRequest().body(response);
        }
    }


    @Override
    public boolean doesProductExist(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.isPresent();
    }
    public boolean doesUserExist(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    private static Product copyProductForDB(ProductDTO productDTO) {
        // Create a new Product entity
        Product product = new Product();

        // Set simple properties directly
        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setState(productDTO.getState());

        // Create Rating entity and set properties
        Rating rating = new Rating();
        rating.setRate(productDTO.getRating().getRate());
        rating.setCount(productDTO.getRating().getCount());

        // Create Inventory entity and set properties
        Inventory inventory = new Inventory();
        inventory.setTotal(productDTO.getInventory().getTotal());
        inventory.setAvailable(productDTO.getInventory().getAvailable());

        // Set Rating and Inventory entities to the Product
        product.setRating(rating);
        product.setInventory(inventory);
        return product;
    }
}
