package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
        return productRepository.findAll().stream()
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
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        log.info("Save Product in Database {}", requestId);
        if (product.getTitle() == null || product.getTitle().isEmpty()) {
            log.error("Product title cannot be empty {}", requestId);
            return new ResponseEntity<>(validationError.getStructureError(HttpStatus.BAD_REQUEST.value(),
                    "Product title cannot be empty"), HttpStatus.BAD_REQUEST);
        }
        Product savedProduct = productRepository.save(product);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
}
