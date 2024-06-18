package pl.smyk.servicedeskfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCredentialsDto {
    private String usernameOrEmail;
    private String password;
}
