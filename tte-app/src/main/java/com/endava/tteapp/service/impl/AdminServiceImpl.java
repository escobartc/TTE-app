package com.endava.tteapp.service.impl;

import com.endava.tteapp.LoggerPrinter;
import com.endava.tteapp.model.User;
import com.endava.tteapp.processor.ValidationError;
import com.endava.tteapp.repository.AdminRepository;
import com.endava.tteapp.service.LegacyAdmin;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AdminServiceImpl implements LegacyAdmin {

    final AdminRepository adminRepository;
    final ValidationError validationError;
    public ResponseEntity<Object> saveUser(User user, LoggerPrinter loggerPrinter) {
        loggerPrinter.log(LogLevel.INFO,"Save Information in Database");
        if (adminRepository.findElement(user.getEmail()) != null){
            loggerPrinter.log(LogLevel.WARN,"Email duplicated");
        return new ResponseEntity<>(validationError.getStructureError((HttpStatus.BAD_REQUEST.value()),
                    "Email exist in database",""), HttpStatus.BAD_REQUEST);
        }
        if (adminRepository.findElement(user.getUsername()) != null){
            loggerPrinter.log(LogLevel.WARN,"Username duplicated");
            return new ResponseEntity<>(validationError.getStructureError((HttpStatus.BAD_REQUEST.value()),
                    "Username exist in database",""), HttpStatus.BAD_REQUEST);
        }
        if(!user.getRole().equals("Employee")){
            loggerPrinter.log(LogLevel.WARN,"invalid role");
            return new ResponseEntity<>(validationError.getStructureError((HttpStatus.BAD_REQUEST.value()),
                    "invalid role",""), HttpStatus.BAD_REQUEST);
        }
        User response = adminRepository.save(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

