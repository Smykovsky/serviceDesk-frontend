package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import pl.smyk.servicedeskfrontend.manager.ViewManager;
import pl.smyk.servicedeskfrontend.rest.Authenticator;
import pl.smyk.servicedeskfrontend.rest.AuthenticatorImpl;
import pl.smyk.servicedeskfrontend.session.SessionManager;

import java.net.URL;
import java.util.List;
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
    @FXML
    private Button previousViewButton;
    private ViewManager viewManager;

    public MainController() {
        restTemplate = new RestTemplate();
        authenticator = new AuthenticatorImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewManager = new ViewManager(this.contentAnchorPane);
        if (SessionManager.getInstance().getUserRoles().contains("OPERATOR")) {
            viewManager.loadView("operatorDashboard-view.fxml");
        } else  viewManager.loadView("userDashboard-view.fxml");

        initializeDashboardButton();
        initializeChartsButton();
        initializeUserPanelButton();
        initializeLogoutButton();
        initializePresiousViewButton();
    }

    private void initializePresiousViewButton() {
        previousViewButton.setOnMouseClicked(x -> {
            viewManager.goBack();
        });
    }


    private void initializeDashboardButton() {
        dashboardButton.setOnAction((x) -> {
            List<String> userRoles = SessionManager.getInstance().getUserRoles();
            if (userRoles.contains("OPERATOR")) {
                viewManager.loadView("operatorDashboard-view.fxml");
            } else {
                viewManager.loadView("userDashboard-view.fxml");
            }

        });
    }

    private void initializeChartsButton() {
        chartsButton.setOnAction((x) -> {
            viewManager.loadView("article/articles-view.fxml");
        });
    }

    private void initializeUserPanelButton() {
        userPanelButton.setOnAction((x) -> {
            viewManager.loadView("userPanel-view.fxml");
        });
    }

    private void initializeLogoutButton() {
        logoutButton.setOnAction((x) -> {
            authenticator.handleLogout();
            try {
                viewManager.loadAuthScene();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Stage getMainStage() {
        return (Stage) mainAnchorPane.getScene().getWindow();
    }

    private Stage getContentStage() {
        return (Stage) contentAnchorPane.getScene().getWindow();
    }
}
