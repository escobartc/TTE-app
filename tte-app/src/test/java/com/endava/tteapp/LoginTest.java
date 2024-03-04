package com.endava.tteapp;

import com.endava.tteapp.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LoginTest {


    @Test
    void userTest(){
        User user = new User(1L,"email","username","password","role");
        user.setId(1L);
        user.setEmail("email");
        user.setUsername("username");
        user.setPassword("password");
        user.setRole("role");
        assertEquals(1L,user.getId());
        assertEquals("email",user.getEmail());
        assertEquals("username",user.getUsername());
        assertEquals("password",user.getPassword());
        assertEquals("role",user.getRole());
    }
}
