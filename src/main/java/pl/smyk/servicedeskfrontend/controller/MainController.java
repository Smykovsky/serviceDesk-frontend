package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.rest.Authenticator;
import pl.smyk.servicedeskfrontend.rest.AuthenticatorImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private RestTemplate restTemplate;
    private Authenticator authenticator;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private AnchorPane menuAnchorPane;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button chartsButton;

    @FXML
    private Button userPanelButton;

    @FXML
    private Button logoutButton;

    public MainController() {
        restTemplate = new RestTemplate();
        authenticator = new AuthenticatorImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadView("dashboard-view.fxml");
        initializeDashboardButton();
        initializeChartsButton();
        initializeUserPanelButton();
        initializeLogoutButton();
    }



    private void initializeDashboardButton() {
        dashboardButton.setOnAction((x) -> {
            loadView("dashboard-view.fxml");
        });
    }

    private void initializeChartsButton() {
        chartsButton.setOnAction((x) -> {
            loadView("charts-view.fxml");
        });
    }

    private void initializeUserPanelButton() {
        userPanelButton.setOnAction((x) -> {
            loadView("userPanel-view.fxml");
        });
    }

    private void initializeLogoutButton() {
        logoutButton.setOnAction((x) -> {
            authenticator.handleLogout();
            try {
                loadAuthScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
            AnchorPane newPane = fxmlLoader.load();
            contentAnchorPane.getChildren().clear();
            contentAnchorPane.getChildren().add(newPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAuthScene() throws IOException {
        Stage mainStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("auth-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        mainStage.setTitle("Service Desk");
        mainStage.setScene(scene);
        mainStage.show();
        getMainStage().close();
    }

    private Stage getMainStage() {
        if (mainAnchorPane != null) {
            return (Stage) mainAnchorPane.getScene().getWindow();
        } else {
            return null;
        }
    }

    private Stage getContentStage() {
        return (Stage) contentAnchorPane.getScene().getWindow();
    }
}
