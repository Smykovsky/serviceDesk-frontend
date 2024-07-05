package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.dto.ReportDto;
import pl.smyk.servicedeskfrontend.manager.ViewManager;
import pl.smyk.servicedeskfrontend.session.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportDetailsController implements Initializable{
    @FXML
    private AnchorPane reportDetailsAnchorPane;
    @FXML
    private Button closeButton;
    @FXML
    private Button assignButton;
    @FXML
    private Button leaveButton;
    @FXML
    private Label idLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label createdByLabel;
    @FXML
    private Label assignedUserLabel;
    @FXML
    private Label priorityLabel;
    @FXML
    private Label statusLabel;

    private ViewManager viewManager;

    public void setReportDetails(ReportDto report) {
        idLabel.setText(String.valueOf(report.getId()));
        titleLabel.setText(report.getTitle());
        descriptionLabel.setText(report.getDescription());
        createdByLabel.setText(report.getCreatedBy());
        assignedUserLabel.setText(report.getAssignedUser());
        priorityLabel.setText(report.getPriority());
        statusLabel.setText(report.getStatus());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewManager = new ViewManager(this.reportDetailsAnchorPane);

        if (!SessionManager.getInstance().getUserRoles().contains("OPERATOR")) {
            assignButton.setVisible(false);
            closeButton.setVisible(false);
        }

        initializeAssignButton();
        initializeCloseButton();
        initializeLeaveButton();
    }


    private void initializeAssignButton() {
        assignButton.setOnAction(x -> {
            try {
                FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("assignReport-view.fxml"));
                AnchorPane root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                AssignReportController controller = loader.getController();
                controller.setReportId(Long.valueOf(idLabel.getText()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initializeLeaveButton() {
        leaveButton.setOnAction(x -> {
            if (SessionManager.getInstance().getUserRoles().contains("OPERATOR")) {
                viewManager.loadView("operatorDashboard-view.fxml");
            } else {
                viewManager.loadView("userDashboard-view.fxml");
            }
        });
    }

    private void initializeCloseButton() {
        closeButton.setOnAction(x -> {
            //handle close report
        });
    }


    private Stage getStage() {
        return (Stage) reportDetailsAnchorPane.getScene().getWindow();
    }
}
