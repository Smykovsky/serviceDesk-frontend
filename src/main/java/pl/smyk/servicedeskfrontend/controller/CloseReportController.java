package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import pl.smyk.servicedeskfrontend.manager.ViewManager;
import pl.smyk.servicedeskfrontend.rest.OperatorRestClient;

import java.net.URL;
import java.util.ResourceBundle;

public class CloseReportController implements Initializable {
    @FXML
    private Button closeButton;
    @FXML
    private Button leaveButton;
    @FXML
    private AnchorPane closeReportAnchorPane;

    private ViewManager viewManager;
    private OperatorRestClient operatorRestClient;
    private Long selectedReportId;

    public CloseReportController() {
        operatorRestClient = new OperatorRestClient();
    }

    public void setReportId(Long id) {
        selectedReportId = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewManager = new ViewManager(closeReportAnchorPane);

        initializeCloseButton();
        initializeLeaveButton();
    }


    private void initializeCloseButton() {
        closeButton.setOnAction(x -> {
            Thread thread = new Thread(() -> {
                operatorRestClient.closeReport(selectedReportId);
                Platform.runLater(() -> {
                    getStage().close();
                });
            });
            thread.start();
        });
    }

    private void initializeLeaveButton() {
        leaveButton.setOnAction(x -> {
            getStage().close();
        });
    }

    private Stage getStage() {
        return (Stage) closeReportAnchorPane.getScene().getWindow();
    }
}
