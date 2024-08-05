package com.order.api.auth.dto;


import com.order.api.auth.entity.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
