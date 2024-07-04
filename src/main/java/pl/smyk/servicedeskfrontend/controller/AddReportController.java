package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.dto.ReportDto;
import pl.smyk.servicedeskfrontend.rest.UserReportRestClient;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddReportController implements Initializable {
    @FXML
    private AnchorPane addFormAnchorPane;
    @FXML
    private Button addButton;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<String> priorityComboBox;
    private final UserReportRestClient userReportRestClient;

    public AddReportController() {
        userReportRestClient = new UserReportRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialzieComboBox();
        initializeAddButton();
    }

    private void initialzieComboBox() {
        List<String> priorities = List.of("LOW_PRIORITY", "MEDIUM_PRIORITY", "HIGH_PRIORITY");
        priorityComboBox.setItems(FXCollections.observableArrayList(priorities));
    }

    private void initializeAddButton() {
        addButton.setOnAction(x -> {
            ReportDto dto = new ReportDto();
            dto.setTitle(titleField.getText());
            dto.setDescription(descriptionArea.getText());
            dto.setPriority(priorityComboBox.getSelectionModel().getSelectedItem());
            System.out.println(priorityComboBox.getSelectionModel().getSelectedItem());

            Thread thread = new Thread(() -> {
                userReportRestClient.createReport(dto);
                Platform.runLater(() -> {
                    getStage().close();
                });
            });
            thread.start();


        });
    }


    public Stage getStage() {
        return (Stage) this.addFormAnchorPane.getScene().getWindow();
    }
}
