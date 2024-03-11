package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.Inventory;
import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.Rating;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Slf4j
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ValidationError validationError;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findByInventoryAvailableGreaterThan(0).stream()
                .map(this::mapToProductDTO)
                .toList();
    }


    // Method to map Product entity to ProductDTO
    private ProductDTO mapToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setTitle(product.getTitle());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategory(product.getCategory());
        return productDTO;
    }

    public ResponseEntity<Object> saveProduct(ProductDTO productDTO, String requestId) {
        Product product = copyProductForDB(productDTO);

        // Save the Product entity
        Product savedProduct = productRepository.save(product);

        // Return ResponseEntity with the saved Product
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    private static Product copyProductForDB(ProductDTO productDTO) {
        // Create a new Product entity
        Product product = new Product();

        // Set simple properties directly
        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
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
