package pl.smyk.servicedeskfrontend.handler;

import pl.smyk.servicedeskfrontend.dto.AuthResponse;

@FunctionalInterface
public interface AuthenticationResultHandler {
    void handle(AuthResponse dto);
}
