package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.configuration.DataConfig;
import com.challenge.tteapp.model.Category;
import com.challenge.tteapp.model.Inventory;
import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.Rating;
import com.challenge.tteapp.model.dto.*;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumeApiServiceImpl {

    private final DataConfig dataConfig;
    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final ProductService productService;

    public void consumeApi() {
        String requestId = UUID.randomUUID().toString();
        log.info("Consume FAKESTORE API, with requestId: [{}]", requestId);

        ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(dataConfig.getPathStore(), Object[].class);
        Object[] objects = responseEntity.getBody();
        for (Object obj : objects) {
        ProductDTOS productDTO = objectMapper.convertValue(obj, ProductDTOS.class);
        ProductDTO modifiedProductDTO = prepareProductDTO(productDTO);
        saveProduct(modifiedProductDTO, requestId);
        }
        log.info("FAKESTORE API consumption completed successfully, with requestId: [{}]", requestId);
    }


    private ProductDTO prepareProductDTO(ProductDTOS productDTO) {
        ProductDTO modifiedProductDTO = new ProductDTO();
        BeanUtils.copyProperties(productDTO, modifiedProductDTO);

        CategoryDTO category = createCategoryDTO(productDTO.getCategory());
        modifiedProductDTO.setState("Approved");
        modifiedProductDTO.setCategory(category);

        RatingDTO rating = createRatingDTO();
        modifiedProductDTO.setRating(rating);

        InventoryDTO inventoryDTO = createInventoryDTO();
        modifiedProductDTO.setInventory(inventoryDTO);

        return modifiedProductDTO;
    }

    private CategoryDTO createCategoryDTO(String categoryName) {
        CategoryDTO category = new CategoryDTO();
        category.setName(categoryName);
        category.setState("Approved");
        return category;
    }

    private RatingDTO createRatingDTO() {
        RatingDTO rating = new RatingDTO();
        rating.setCount(0);
        rating.setRate(0);
        return rating;
    }

    private InventoryDTO createInventoryDTO() {
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setAvailable(10);
        inventoryDTO.setTotal(10);
        return inventoryDTO;
    }

    private void saveProduct(ProductDTO productDTO, String requestId) {
        productService.saveProduct(productDTO, requestId);
    }


}
