package com.example.MyGreetingApp.DTOs;


import lombok.Data;

@Data
public class PasswordResetDTO {
    private String currentPassword;
    private String newPassword;
}
