package pl.smyk.servicedeskfrontend.rest;

import pl.smyk.servicedeskfrontend.dto.UserCredentialsDto;
import pl.smyk.servicedeskfrontend.handler.AuthenticationResultHandler;

public interface Authenticator {
    void authenticate(UserCredentialsDto dto, AuthenticationResultHandler handler);
    void handleLogout();
}
