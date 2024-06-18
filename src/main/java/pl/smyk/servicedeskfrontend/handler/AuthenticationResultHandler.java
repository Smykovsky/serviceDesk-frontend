package pl.smyk.servicedeskfrontend.handler;

import pl.smyk.servicedeskfrontend.dto.UserAuthenticationResultDto;

@FunctionalInterface
public interface AuthenticationResultHandler {
    void handle(UserAuthenticationResultDto dto);
}
