package pl.smyk.servicedeskfrontend.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.smyk.servicedeskfrontend.dto.AuthResponse;
import pl.smyk.servicedeskfrontend.dto.UserCredentialsDto;
import pl.smyk.servicedeskfrontend.handler.AuthenticationResultHandler;
import pl.smyk.servicedeskfrontend.session.SessionManager;

public class AuthenticatorImpl implements Authenticator{
    public static final String AUTH_URL = "http://localhost:8080/api/auth";

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

    @Override
    public void handleLogout() {
        try {
            String accessToken = SessionManager.getInstance().getAccessToken();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Baerer " + accessToken);
            HttpEntity<String> request = new HttpEntity<>(httpHeaders);

            restTemplate.exchange(AUTH_URL + "/logout", HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SessionManager.getInstance().clearSession();
        }
    }

    private void authProcess(UserCredentialsDto dto, AuthenticationResultHandler handler) {
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(AUTH_URL + "/login", dto, AuthResponse.class);

        if (response.getStatusCode().is2xxSuccessful()){
            SessionManager.getInstance().setAccessToken(response.getBody().getAccessToken());
            SessionManager.getInstance().setRefreshToken(response.getBody().getRefreshToken());
            handler.handle(response.getBody());
        } else {
            System.out.println("ERROR!");
        }
    }


}
