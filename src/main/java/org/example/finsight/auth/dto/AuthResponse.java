package org.example.finsight.auth.dto;

public class AuthResponse {

    public AuthResponse(String token) {
        this.token = token;
    }
    private String token;

    public String getToken() {
        return token;
    }
}