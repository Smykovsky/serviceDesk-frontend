package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import pl.smyk.servicedeskfrontend.manager.ViewManager;
import pl.smyk.servicedeskfrontend.session.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class UserPanelController implements Initializable {
    @FXML
    private AnchorPane userPanelAnchorPane;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField dobField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextArea addressArea;
    @FXML
    private Button logoutButton;

    private ViewManager viewManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewManager = new ViewManager(userPanelAnchorPane);
        initializeLogoutButton();
    }

    private void initializeLogoutButton() {
        logoutButton.setOnAction(x -> {

        });
    }


}
