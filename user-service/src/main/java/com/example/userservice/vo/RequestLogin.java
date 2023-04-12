package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {

    @NotNull(message = "Email is required")
    @Size(min = 2, message = "Email could not be less than 2 characters")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password could not be less than 8 characters")
    private String password;
}
