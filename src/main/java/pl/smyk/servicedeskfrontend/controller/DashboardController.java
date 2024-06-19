package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.smyk.servicedeskfrontend.MainApp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
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
            //logout logic
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

    private Stage getStage() {
        return (Stage) mainAnchorPane.getScene().getWindow();
    }
}
