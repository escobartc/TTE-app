package com.challenge.tteapp.model;

import com.challenge.tteapp.model.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ViewUsers {
    private List<UserDTO> users;
}
