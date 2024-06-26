package pl.smyk.servicedeskfrontend.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pl.smyk.servicedeskfrontend.dto.AuthResponse;
import pl.smyk.servicedeskfrontend.dto.UserCredentialsDto;
import pl.smyk.servicedeskfrontend.factory.PopupFactory;
import pl.smyk.servicedeskfrontend.handler.AuthenticationResultHandler;
import pl.smyk.servicedeskfrontend.session.SessionManager;

import java.util.List;

public class AuthenticatorImpl implements Authenticator{
    public static final String AUTH_URL = "http://localhost:8080/api/auth";

    private final RestTemplate restTemplate;
    private PopupFactory popupFactory;

    public AuthenticatorImpl() {
        restTemplate = new RestTemplate();
        popupFactory = new PopupFactory();
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
        try {
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(AUTH_URL + "/login", dto, AuthResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                SessionManager.getInstance().setAccessToken(response.getBody().getAccessToken());
                SessionManager.getInstance().setRefreshToken(response.getBody().getRefreshToken());
                DecodedJWT decodedJWT = decodeJwtToken(response.getBody().getAccessToken());
                List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
                SessionManager.getInstance().setUserRoles(roles);
                handler.handle(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            System.out.println("HTTP error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            Platform.runLater(() -> {
                Stage infoPopup = popupFactory.createInfoPopup("Incorrect credentials! Try again!");
                infoPopup.show();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private DecodedJWT decodeJwtToken(String token) {
        return JWT.decode(token);
    }


}
