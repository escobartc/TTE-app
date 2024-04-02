package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.dto.*;
import com.challenge.tteapp.model.response.MessageResponse;
import com.challenge.tteapp.repository.CategoryRepository;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.repository.ReviewRepository;
import com.challenge.tteapp.repository.UserRepository;
import com.challenge.tteapp.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

@AllArgsConstructor
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductDTO)
                .toList();
    }

    @Override
    public Page<ProductDTO> getAllProductsWithOrder(String orderBy, Pageable pageable) {
        Sort sort;
        if (!orderBy.isEmpty()) {
            String[] orderByParts = orderBy.split(",");
            String property = orderByParts[0];
            String direction = "asc"; // Default direction is ascending
            if (orderByParts.length > 1) {
                direction = orderByParts[1];
            }
            if ("desc".equalsIgnoreCase(direction)) {
                sort = Sort.by(property).descending();
            } else {
                sort = Sort.by(property).ascending();
            }
        } else {
            sort = Sort.by("id");
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return productRepository.findAll(pageable).map(this::mapProductDTOCustomer);
    }

    @Override
    public Optional<ProductDTO> getProduct(Long product_id) {
        Optional<Product> product = productRepository.findById(product_id);
        if (product.isPresent()) {
            ProductDTO productDTO = mapProductDTOCustomer(product.get());
            return Optional.of(productDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> getProductReviews(Long productId, String requestId) {
        log.info("products reviews with requestId: [{}]", requestId);
        List<ReviewDTO> reviews = getProductReviewsById(productId);
        if (reviews.isEmpty()) {
            log.warn("No reviews found for product, with requestId: [{}]", requestId);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No reviews found for product");
        } else {
            log.info("reviews found for product, with requestId: [{}]", requestId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<MessageResponse> addProductReview(Long productId, ReviewDTO reviewDTO, String requestId, String email) {
        log.info("add product review, with requestId: [{}]", requestId);
        Review review = new Review();
        doesUserExist(reviewDTO.getUser(), email);
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            review.setProduct_id(product);
            review.setUser(reviewDTO.getUser());
            review.setComment(reviewDTO.getComment());
            Review savedReview = reviewRepository.save(review);
            log.info("add product successful added, with requestId: [{}]", requestId);
            return new ResponseEntity<>(new MessageResponse("review: " + savedReview.getId() + " successful added"), HttpStatus.CREATED);
        } else {
            log.error("Invalid product ID: " + productId + ", with requestId: [{}]", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid product ID: " + productId);
        }
    }

    public ProductDTO mapProductDTOCustomer(Product product) {
        ProductDTO productDTO = mapToProductDTO(product);
        productDTO.setImage(product.getImage());
        productDTO.setDescription(product.getDescription());
        productDTO.setRating(mapToRatingDTO(product.getRating()));
        productDTO.setInventory(mapToInventoryDTO(product.getInventory()));
        return productDTO;
    }

    private ProductDTO mapToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setTitle(product.getTitle());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategory(mapToCategoryDTO(product.getCategory()));
        return productDTO;
    }

    private CategoryDTO mapToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    private InventoryDTO mapToInventoryDTO(Inventory inventory) {
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setId(inventory.getId());
        inventoryDTO.setAvailable(inventory.getAvailable());
        inventoryDTO.setTotal(inventory.getTotal());
        return inventoryDTO;
    }

    private RatingDTO mapToRatingDTO(Rating rating) {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(rating.getId());
        ratingDTO.setRate(rating.getRate());
        ratingDTO.setCount(rating.getCount());
        return ratingDTO;
    }

    public ResponseEntity<Object> saveProduct(ProductDTO productDTO, String requestId) {
        Product product = copyProductForDB(productDTO);

        Category category = categoryRepository.findByName(productDTO.getCategory().getName());
        if (category == null) {
            category = new Category();
            category.setName(productDTO.getCategory().getName());
            category.setState(productDTO.getState());
            category = categoryRepository.save(category);
        }
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    public static Product copyProductForDB(ProductDTO productDTO) {
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

    private void doesUserExist(String username, String email) {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<User> user1 = userRepository.findByEmail(email);
        if (user.isEmpty() || user1.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User with provided username does not exist");
        } else {
            if (!user.get().getUsername().equals(user1.get().getUsername())) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Mismatch between provided userId and email");
            }
        }
    }

    private List<ReviewDTO> getProductReviewsById(Long productId) {
        return productRepository.findById(productId)
                .map(product -> product.getReviews().stream()
                        .map(review -> convertToReviewDTO(review, productId))
                        .toList()
                )
                .orElse(Collections.emptyList());
    }

    public ReviewDTO convertToReviewDTO(Review review, Long productId) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUser(review.getUser());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setProductId(productId);
        return reviewDTO;
    }

}
