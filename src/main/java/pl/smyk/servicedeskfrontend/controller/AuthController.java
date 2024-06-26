package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import pl.smyk.servicedeskfrontend.manager.ViewManager;
import pl.smyk.servicedeskfrontend.dto.UserCredentialsDto;
import pl.smyk.servicedeskfrontend.factory.PopupFactory;
import pl.smyk.servicedeskfrontend.rest.Authenticator;
import pl.smyk.servicedeskfrontend.rest.AuthenticatorImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    private Authenticator authenticator;
    private PopupFactory popupFactory;

    @FXML
    private AnchorPane loginAnchorPane;

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button exitButton;

    private ViewManager viewManager;

    public AuthController() {
        authenticator = new AuthenticatorImpl();
        popupFactory = new PopupFactory();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewManager = new ViewManager(this.loginAnchorPane);
        initializeLoginButton();
        initializeExitButton();
    }



    private void initializeLoginButton() {
        loginButton.setOnAction((x) -> {
            authenticateUser();
        });
    }

    private void authenticateUser() {
//        UserCredentialsDto dto = new UserCredentialsDto(loginTextField.getText(), passwordTextField.getText());
        UserCredentialsDto dto = new UserCredentialsDto("operator.operator", "12345678");
            authenticator.authenticate(dto, (authenticationResult) -> {
                System.out.println(authenticationResult);
                Platform.runLater(() -> {
                    if (authenticationResult.getIsAuthenticated()) {
                        viewManager.loadScene("main-view.fxml");
                    }
                });
            });
    }

    private void initializeExitButton() {
        exitButton.setOnAction((x) -> {
            getStage().close();
        });
    }

    private Stage getStage() {
        return (Stage) loginAnchorPane.getScene().getWindow();
    }
}
