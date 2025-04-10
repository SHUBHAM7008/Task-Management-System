package com.example.Task.Management.New.dto;


import com.example.Task.Management.New.model.User;
import lombok.Data;

import java.util.Optional;

@Data
public class AuthResponse {
    private String token;
    private Optional<User> user;

    public AuthResponse(String token,User user) {
        this.token = token;
        this.user= Optional.ofNullable(user);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Optional<User> getUser() {
        return user;
    }

    public void setUser(Optional<User> user) {
        this.user = user;
    }
}
