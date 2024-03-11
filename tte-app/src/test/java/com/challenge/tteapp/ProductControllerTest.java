package com.challenge.tteapp;

import com.challenge.tteapp.controller.ProductController;
import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.dto.InventoryDTO;
import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.model.dto.RatingDTO;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    @Test
    public void testCreateProduct_Success() {
        ProductDTO productDTO = getProductDTOForTest();

        Product savedProduct = new Product();
        savedProduct.setId(1L); // Assuming product ID is set upon creation
        ResponseEntity<Object> successResponse = new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        when(productService.saveProduct(eq(productDTO), anyString())).thenReturn(successResponse);

        ResponseEntity<Object> response = productController.createProduct(productDTO, Mockito.mock(Authentication.class));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("productId"));
        assertEquals(1L, responseBody.get("productId")); // Assuming the product ID is 1
        assertEquals("Product created successfully", responseBody.get("message"));
    }

    private static ProductDTO getProductDTOForTest() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setTitle("Sample Product");
        productDTO.setPrice(new BigDecimal("19.99"));
        productDTO.setDescription("A wonderful sample product description.");
        productDTO.setCategory("Electronics");
        productDTO.setImage("https://example.com/sample-product.jpg");
        productDTO.setState("Approved");

        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setRate(4.5);
        ratingDTO.setCount(100);
        productDTO.setRating(ratingDTO);

        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setTotal(50);
        inventoryDTO.setAvailable(25);
        productDTO.setInventory(inventoryDTO);
        return productDTO;
    }

    @Test
    public void testGetAllProducts_Success() {
        // Mock data
        List<ProductDTO> products = new ArrayList<>();
        // Add mock products to the list

        // Mock service method
        when(productService.getAllProducts()).thenReturn(products);

        // Perform GET request to /api/product
        ResponseEntity<List<ProductDTO>> response = productController.getAllProducts();

        // Assert response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }


    @Test
    public void testUpdateProduct_Success() {
        // Mock data
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L); // Existing product ID

        // Mock service method
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product())); // Existing product
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(new Product());

        // Perform PUT request to /api/product
        ResponseEntity<Object> response = productController.updateProduct(productDTO);

        // Assert response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteProduct_Success() {
        // Mock data
        Long productId = 1L;

        // Mock service method
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product())); // Existing product
        Mockito.doNothing().when(productRepository).deleteById(anyLong());

        // Perform DELETE request to /api/product
        ResponseEntity<Object> response = productController.deleteProduct(Collections.singletonMap("id", productId), mock(Authentication.class));

        // Assert response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteProduct_NotFound() {
        // Mock data
        Long productId = 1L;

        // Mock service method
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty()); // Product not found

        // Perform DELETE request to /api/product
        ResponseEntity<Object> response = productController.deleteProduct(Collections.singletonMap("id", productId), mock(Authentication.class));

        // Assert response status code
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
