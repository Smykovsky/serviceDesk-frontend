package pl.smyk.servicedeskfrontend.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.smyk.servicedeskfrontend.dto.UserAuthenticationResultDto;
import pl.smyk.servicedeskfrontend.dto.UserCredentialsDto;
import pl.smyk.servicedeskfrontend.handler.AuthenticationResultHandler;

public class AuthenticatorImpl implements Authenticator{
    public static final String AUTH_URL = "http://localhost:8080/api/auth/login";

    private final RestTemplate restTemplate;

    public AuthenticatorImpl() {
        restTemplate = new RestTemplate();
    }

    @Override
    public void authenticate(UserCredentialsDto dto, AuthenticationResultHandler handler) {
        Runnable authTask = () -> {
            authProcess(dto, handler);
        };

        Thread authThread = new Thread(authTask);
        authThread.setDaemon(true);
        authThread.start();
    }

    private void authProcess(UserCredentialsDto dto, AuthenticationResultHandler handler) {
        ResponseEntity<UserAuthenticationResultDto> response = restTemplate.postForEntity(AUTH_URL, dto, UserAuthenticationResultDto.class);
        handler.handle(response.getBody());
    }
}
