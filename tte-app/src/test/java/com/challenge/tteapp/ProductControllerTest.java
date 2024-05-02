package com.challenge.tteapp;

import com.challenge.tteapp.controller.ProductController;
import com.challenge.tteapp.model.*;
import com.challenge.tteapp.model.dto.*;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

//    @Test
//    void testCreateProduct_Success() {
//        // Mock data
//        ProductDTO productDTO = getProductDTOForTest();
//        Product savedProduct = new Product();
//        savedProduct.setId(1L);
//        ResponseEntity<Object> successResponse = new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
//        when(productService.saveProduct(eq(productDTO), anyString())).thenReturn(successResponse);
//
//        // Perform POST request to /api/product
//        ResponseEntity<Object> response = productController.createProduct(productDTO, Mockito.mock(Authentication.class));
//
//        // Assert response status code and body
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
//        assertNotNull(responseBody);
//        assertTrue(responseBody.containsKey("productId"));
//        assertEquals(1L, responseBody.get("productId"));
//        assertEquals("Product created successfully", responseBody.get("message"));
//    }

    @Test
    void testGetAllProducts_Success() {
        // Mock data
        List<ProductDTO> products = Arrays.asList(getProductDTOForTest(), getProductDTOForTest()); // Mocked products
        when(productService.getAllProducts()).thenReturn(products);

        // Perform GET request to /api/product
        ResponseEntity<List<ProductDTO>> response = productController.getAllProducts();

        // Assert response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    void testUpdateProduct_Success() {
        // Mock data
        ProductDTO productDTO = getProductDTOForTest();
        productDTO.setId(1L); // Existing product ID

        // Mock repository to return an existing product
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        when(productRepository.save(any(Product.class))).thenReturn(new Product());

        // Perform PUT request to /api/product
        ResponseEntity<Object> response = productController.updateProduct(productDTO);

        // Assert response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteProduct_Success() {
        // Mock data
        Long productId = 1L;

        // Mock repository to return an existing product
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        doNothing().when(productRepository).deleteById(anyLong());

        // Perform DELETE request to /api/product
        ResponseEntity<Object> response = productController.deleteProduct(Collections.singletonMap("id", productId), mock(Authentication.class));

        // Assert response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteProduct_NotFound() {
        // Mock data
        Long productId = 1L;

        // Mock repository to return no product
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Perform DELETE request to /api/product
        ResponseEntity<Object> response = productController.deleteProduct(Collections.singletonMap("id", productId), mock(Authentication.class));

        // Assert response status code
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private static ProductDTO getProductDTOForTest() {
        // Create and return a sample ProductDTO
        return new ProductDTO(
                1L,
                "Sample Product",
                new BigDecimal("19.99"),
                "A wonderful sample product description.",
                new CategoryDTO(1L, "Electronics", "Approved"),
                "https://example.com/sample-product.jpg",
                "Approved",
                new RatingDTO(1L, 4.5, 100),
                new InventoryDTO(1L, 50, 25)
        );
    }
}
