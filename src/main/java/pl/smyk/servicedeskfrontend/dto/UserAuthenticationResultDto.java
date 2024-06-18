package pl.smyk.servicedeskfrontend.dto;

import lombok.Data;

@Data
public class UserAuthenticationResultDto {
    private String accessToken;
    private String refreshToken;
    private String message;
}
