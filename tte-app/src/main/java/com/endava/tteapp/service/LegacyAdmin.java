package com.endava.tteapp.service;

import com.endava.tteapp.LoggerPrinter;
import com.endava.tteapp.model.User;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface LegacyAdmin  {
    ResponseEntity<Object> saveUser(User user, LoggerPrinter loggerPrinter);
}
