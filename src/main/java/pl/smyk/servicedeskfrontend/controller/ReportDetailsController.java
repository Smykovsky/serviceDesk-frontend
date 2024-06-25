package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import pl.smyk.servicedeskfrontend.dto.ReportDto;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportDetailsController {
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

    public void setReportDetails(ReportDto report) {
        idLabel.setText(String.valueOf(report.getId()));
        titleLabel.setText(report.getTitle());
        descriptionLabel.setText(report.getDescription());
        createdByLabel.setText(report.getCreatedBy());
        assignedUserLabel.setText(report.getAssignedUser());
        priorityLabel.setText(report.getPriority());
        statusLabel.setText(report.getStatus());
    }
}
