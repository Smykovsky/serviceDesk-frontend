package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import pl.smyk.servicedeskfrontend.factory.PopupFactory;
import pl.smyk.servicedeskfrontend.manager.ViewManager;
import pl.smyk.servicedeskfrontend.rest.OperatorRestClient;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CloseReportController implements Initializable {
    @FXML
    private Button closeButton;
    @FXML
    private Button updateButton;
    @FXML
    private AnchorPane closeReportAnchorPane;
    @FXML
    private ComboBox<String> statusComboBox;
    private ViewManager viewManager;
    private OperatorRestClient operatorRestClient;
    private Long selectedReportId;
    private String selectedStatus;
    private final PopupFactory popupFactory;

    public CloseReportController() {
        operatorRestClient = new OperatorRestClient();
        popupFactory = new PopupFactory();
    }

    public void setReportId(Long id) {
        selectedReportId = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewManager = new ViewManager(closeReportAnchorPane);

        initializeComboBox();
        initializeCloseButton();
        initializeUpdateButton();
    }


    private void initializeCloseButton() {
        closeButton.setOnAction(x -> {
            getStage().close();
        });
    }

    private void initializeUpdateButton() {
        updateButton.setOnAction(x -> {
            selectedStatus = statusComboBox.getSelectionModel().getSelectedItem();

            Thread thread = new Thread(() -> {
                ResponseEntity<?> response;
                if (selectedStatus.equals("CLOSED")) {
                    response = operatorRestClient.closeReport(selectedReportId);
                } else if (selectedStatus.equals("IN_PROGRESS")) {
                    response = operatorRestClient.changeReportStatusToInProgress(selectedReportId);
                } else {
                    response = null;
                }
                System.out.println(selectedStatus);
                Platform.runLater(() -> {
                    getStage().close();
                    Stage infoPopup = popupFactory.createInfoPopup(response.getBody().toString());
                    infoPopup.show();
                });
            });
            thread.start();
        });
    }

    private void initializeComboBox() {
        List<String> statusList = List.of("CLOSED", "IN_PROGRESS");
        statusComboBox.setItems(FXCollections.observableArrayList(statusList));

        statusComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("CLOSED")) {
                updateButton.setText("Close");
            } else updateButton.setText("Update");
        });
    }

    private Stage getStage() {
        return (Stage) closeReportAnchorPane.getScene().getWindow();
    }
}
