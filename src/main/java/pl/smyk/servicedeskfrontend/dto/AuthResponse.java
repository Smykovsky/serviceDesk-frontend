package pl.smyk.servicedeskfrontend.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String message;
    private Boolean isAuthenticated;
}
