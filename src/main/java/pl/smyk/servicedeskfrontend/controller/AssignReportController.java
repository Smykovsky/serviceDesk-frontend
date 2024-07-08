package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.dto.AssignReportRequest;
import pl.smyk.servicedeskfrontend.manager.ViewManager;
import pl.smyk.servicedeskfrontend.rest.OperatorRestClient;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Getter
public class AssignReportController implements Initializable {
    @FXML
    private AnchorPane assignReportFormAnchorPane;

    @FXML
    private Button closeButtton;

    @FXML
    private ComboBox<String> operatorComboBox;

    @FXML
    private Button saveButton;
    private final OperatorRestClient operatorRestClient;
    private Long selectedReportId;
    private ViewManager viewManager;

    public AssignReportController() {
        operatorRestClient = new OperatorRestClient();
    }

    public void setReportId(Long id) {
        selectedReportId = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewManager = new ViewManager(assignReportFormAnchorPane);
        initializeComboBox();
        initializeSaveButton();
        initializeCloseButton();
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(event -> {
            String selectedOperator = operatorComboBox.getSelectionModel().getSelectedItem();
            AssignReportRequest dto = new AssignReportRequest(selectedReportId, selectedOperator);
            Thread thread = new Thread(() -> {
                operatorRestClient.assignReportToOperator(dto);
                Platform.runLater(() -> {
                    getStage().close();
                });
            });
            thread.start();
        });
    }

    private void initializeCloseButton() {
        closeButtton.setOnAction(x -> {
            getStage().close();
        });
    }

    private void initializeComboBox() {
        List<String> operatorUsernames = operatorRestClient.loadOperatorUsernames();
        operatorComboBox.setItems(FXCollections.observableArrayList(operatorUsernames));

    }

    private Stage getStage() {
        return (Stage) assignReportFormAnchorPane.getScene().getWindow();
    }
}
