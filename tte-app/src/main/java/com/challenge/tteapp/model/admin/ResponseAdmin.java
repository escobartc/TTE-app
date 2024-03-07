package com.challenge.tteapp.model.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResponseAdmin {

    private String id;
    private String email;
    private String username;
    private String role;

}
