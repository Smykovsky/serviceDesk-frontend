package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.rest.Authenticator;
import pl.smyk.servicedeskfrontend.rest.AuthenticatorImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    private Authenticator authenticator;

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

    public AuthController() {
        authenticator = new AuthenticatorImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLoginButton();
        initializeExitButton();
    }



    private void initializeLoginButton() {
        loginButton.setOnAction((x) -> {
            authenticateUser();
        });
    }

    private void authenticateUser(){
        try {
            loadMainScene();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        UserCredentialsDto dto = new UserCredentialsDto(loginTextField.getText(), passwordTextField.getText());
//        System.out.println(dto);
//        authenticator.authenticate(dto, (authenticationResult) -> {
//            Platform.runLater(() -> {
//                System.out.println("auth result: " + authenticationResult);
//                if (authenticationResult.getAccessToken() != null) {
//                    //then open main app
//                } else {
//                    //return fail
//                }
//            });
//        });
    }

    private void initializeExitButton() {
        exitButton.setOnAction((x) -> {
            getStage().close();
        });
    }

    private void loadMainScene() throws IOException {
        Stage mainStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("dashboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        mainStage.setTitle("Service Desk");
        mainStage.setScene(scene);
        mainStage.show();
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) loginAnchorPane.getScene().getWindow();
    }
}
