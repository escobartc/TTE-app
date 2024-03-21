package com.challenge.tteapp.processor;

import com.challenge.tteapp.service.impl.ConsumeApiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumeApi implements ApplicationRunner {

    private final ConsumeApiServiceImpl consumeApiService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        consumeApiService.consumeApi();
    }
}
