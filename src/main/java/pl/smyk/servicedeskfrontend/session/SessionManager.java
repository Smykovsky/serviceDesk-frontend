package pl.smyk.servicedeskfrontend.session;

public class SessionManager {
    private static SessionManager instance;
    private String accessToken;
    private String refreshToken;

    private SessionManager() {

    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void clearSession() {
        accessToken = null;
        refreshToken = null;
    }
}
