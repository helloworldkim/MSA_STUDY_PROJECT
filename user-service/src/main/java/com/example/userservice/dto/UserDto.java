package com.example.userservice.dto;

import lombok.Data;
import org.springframework.boot.SpringApplication;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private LocalDateTime createdAt;

    private String encryptedPwd;

}
