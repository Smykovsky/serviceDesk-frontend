package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import pl.smyk.servicedeskfrontend.MainApp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private AnchorPane menuAnchorPane;

    @FXML
    private Button view1Button;

    @FXML
    private Button view2Button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeview1Button();
        initializeview2Button();
    }

    private void initializeview1Button() {
        view1Button.setOnAction((x) -> {
            loadView("view1.fxml");
        });
    }

    private void initializeview2Button() {
        view2Button.setOnAction((x) -> {
            loadView("view2.fxml");
        });
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
            Pane newPane = fxmlLoader.load();
            contentAnchorPane.getChildren().clear();
            contentAnchorPane.getChildren().add(newPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
