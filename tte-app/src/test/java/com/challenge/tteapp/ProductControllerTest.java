/*
package com.challenge.tteapp;

import com.challenge.tteapp.controller.ProductController;
import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
 class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private List<ProductDTO> productList;

    @BeforeEach
    void setUp() {
        // Initialize a list of productDTOs for testing
        productList = new ArrayList<>();
        ProductDTO product1 = new ProductDTO();
        product1.setId(1L);
        product1.setTitle("Product 1");
        product1.setPrice(BigDecimal.valueOf(10.99));
        product1.setCategory("Category A");
        productList.add(product1);

        ProductDTO product2 = new ProductDTO();
        product2.setId(2L);
        product2.setTitle("Product 2");
        product2.setPrice(BigDecimal.valueOf(20.99));
        product2.setCategory("Category B");
        productList.add(product2);
    }

    @Test
    void testGetAllProducts() {
        // Mock the behavior of productService.getAllProducts() method
        when(productService.getAllProducts()).thenReturn(productList);

        // Call the getAllProducts() method of the productController
        ResponseEntity<List<ProductDTO>> responseEntity = productController.getAllProducts();

        // Verify that the response status is OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify that the returned product list matches the expected productList
        List<ProductDTO> returnedProductList = responseEntity.getBody();
        assertEquals(productList.size(), returnedProductList.size());
        for (int i = 0; i < productList.size(); i++) {
            ProductDTO expectedProduct = productList.get(i);
            ProductDTO returnedProduct = returnedProductList.get(i);
            assertEquals(expectedProduct.getId(), returnedProduct.getId());
            assertEquals(expectedProduct.getTitle(), returnedProduct.getTitle());
            assertEquals(expectedProduct.getPrice(), returnedProduct.getPrice());
            assertEquals(expectedProduct.getCategory(), returnedProduct.getCategory());
            // Add more assertions for other fields if needed
        }
    }
}
*/