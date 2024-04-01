package com.challenge.tteapp.model.response;

import com.challenge.tteapp.model.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsersListResponse {
    private List<UserDTO> users;
}
