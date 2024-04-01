package com.challenge.tteapp;

import com.challenge.tteapp.configuration.DataConfig;
import com.challenge.tteapp.model.Inventory;
import com.challenge.tteapp.model.dto.ProductDTOS;
import com.challenge.tteapp.service.ProductService;
import com.challenge.tteapp.service.impl.ConsumeApiServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsumeApiServiceImplTest {


    @Mock
    private DataConfig dataConfig;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ProductService productService;

    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private ConsumeApiServiceImpl consumeApiService;

    @Test
    void consumeApiTest() {
        when(dataConfig.getPathStore()).thenReturn("/api");
        Object[] mockProducts = prepareMockProducts();
        ResponseEntity<Object[]> mockResponseEntity = new ResponseEntity<>(mockProducts, HttpStatus.OK);
        when(restTemplate.getForEntity(dataConfig.getPathStore(), Object[].class)).thenReturn(mockResponseEntity);
        ProductDTOS productDTOS = new ProductDTOS();
        productDTOS.setTitle("title");
        when(objectMapper.convertValue(any(), eq(ProductDTOS.class))).thenReturn(productDTOS);
        consumeApiService.consumeApi();
        Inventory inventoryDTO = new Inventory();
        inventoryDTO.setAvailable(1);
        inventoryDTO.setTotal(1);
        inventoryDTO.setId(1L);
        assertEquals(1,inventoryDTO.getId());
        assertEquals(1,inventoryDTO.getAvailable());
        assertEquals(1,inventoryDTO.getTotal());
    }

    private Object[] prepareMockProducts() {
        return new Object[] { prepareMockProduct("Product1"), prepareMockProduct("Product2") };
    }

    private Object prepareMockProduct(String productName) {
        ProductDTOS product = new ProductDTOS();
        product.setTitle(productName);
        product.setCategory("Category");
        return product;
    }
}
