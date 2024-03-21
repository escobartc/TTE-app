package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.configuration.DataConfig;
import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.dto.ProductDTOS;
import com.challenge.tteapp.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumeApiServiceImpl {

    private final DataConfig dataConfig;
    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public void fetchAndProcessProducts() {
        ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(dataConfig.getPathStore(), Object[].class);
        Object[] objects = responseEntity.getBody();
        for (Object obj : objects) {
            ProductDTOS productDTO = objectMapper.convertValue(obj, ProductDTOS.class);
            Product product = new Product();
            BeanUtils.copyProperties(productDTO, product);
           //productRepository.save(product);
        }
    }




}
