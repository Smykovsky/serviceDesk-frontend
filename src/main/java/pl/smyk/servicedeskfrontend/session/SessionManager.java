package pl.smyk.servicedeskfrontend.session;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter @Setter
public class SessionManager {
    private static SessionManager instance;
    private String accessToken;
    private String refreshToken;
    private List<String> userRoles;

    private SessionManager() {

    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void clearSession() {
        accessToken = null;
        refreshToken = null;
        userRoles = null;
    }
}
