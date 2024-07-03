package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.smyk.servicedeskfrontend.MainApp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserDashboardController implements Initializable {
    @FXML
    private Button addButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeAddButton();
    }

    private void initializeAddButton() {
        addButton.setOnAction((x) -> {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("addReportForm-view.fxml"));
            try {
                AnchorPane newPane = fxmlLoader.load();
                Scene scene = new Scene(newPane);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
