package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.dto.ReportDto;
import pl.smyk.servicedeskfrontend.factory.PopupFactory;
import pl.smyk.servicedeskfrontend.manager.ReportCardManager;
import pl.smyk.servicedeskfrontend.manager.ViewManager;
import pl.smyk.servicedeskfrontend.rest.OperatorRestClient;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class OperatorDashboardController implements Initializable {
    @FXML
    private AnchorPane dashboardAnchorPane;
    @FXML
    private Pane allNotAssignedCard;
    @FXML
    private Pane myNotClosedCard;
    @FXML
    private Pane myClosedCard;
    @FXML
    private Pane allAssignedCard;
    @FXML
    private Pane myInProgressCard;
    @FXML
    private Pane allClosedCard;
    @FXML
    private Label allNotAssignedLabel;
    @FXML
    private Label allAssignedByUserLabel;
    @FXML
    private Label allClosedByUserLabel;
    @FXML
    private Label allAssignedLabel;
    @FXML
    private Label inProgressLabel;
    @FXML
    private Label allClosedLabel;
    @FXML
    private PieChart pieChart;
    @FXML
    private ViewManager viewManager;
    private OperatorRestClient operatorRestClient;
    private PopupFactory popupFactory;
    private HashMap<String, Integer> dashboardData;

    public OperatorDashboardController() {
        operatorRestClient = new OperatorRestClient();
        popupFactory = new PopupFactory();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewManager = new ViewManager(this.dashboardAnchorPane);

        openAllNotAssignedReportsView();
        openMyNotClosedReportsView();
        openMyClosedReportsView();
        openAllAssignedReportsView();
        openMyInProgressReportsView();
        openAllSolvedReportsView();
        initializeLabels();
    }

    private void initializePieChart() {
        PieChart.Data solvedData = new PieChart.Data("All solved", dashboardData.get("allClosed"));
        PieChart.Data notAssignedData = new PieChart.Data("All not assigned", dashboardData.get("allNotAssigned"));
        PieChart.Data myAlLClosed = new PieChart.Data("My closed", dashboardData.get("allClosedByUser"));
        PieChart.Data myNotClosed = new PieChart.Data("My not closed", dashboardData.get("allAssignedByUser"));

        pieChart.getData().addAll(solvedData, notAssignedData, myAlLClosed, myNotClosed);

        for (PieChart.Data data : pieChart.getData()) {
            Tooltip tooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
            Tooltip.install(data.getNode(), tooltip);
            data.pieValueProperty().addListener((observable, oldValue, newValue) ->
                    tooltip.setText(data.getName() + ": " + newValue.intValue())
            );
        }

        pieChart.setTitle("Reports chart");
        pieChart.setLabelsVisible(true);
    }

    private void openAllNotAssignedReportsView() {
        allNotAssignedCard.setOnMouseClicked((x) -> {
            loadReports(() -> operatorRestClient.getAllNotAssignedReports(), "report/reportsTable-view.fxml", "All not assigned Reports");
        });
    }

    private void openMyNotClosedReportsView() {
        myNotClosedCard.setOnMouseClicked((x) -> {
            loadReports(() -> operatorRestClient.getMyNotClosedReports(), "report/reportsTable-view.fxml", "My not closed Reports");
        });
    }

    private void openMyClosedReportsView() {
        myClosedCard.setOnMouseClicked((x) -> {
            loadReports(() -> operatorRestClient.getMyAllClosedReports(), "report/reportsTable-view.fxml", "My all closed Reports");
        });
    }

    private void openAllAssignedReportsView() {
        allAssignedCard.setOnMouseClicked(x -> {
            loadReports(() -> operatorRestClient.getAllAssignedReports(), "report/reportsTable-view.fxml", "All Assigned Reports");
        });
    }

    private void openMyInProgressReportsView() {
        myInProgressCard.setOnMouseClicked((x) -> {
            loadReports(() -> operatorRestClient.getMyInProgressReports(), "report/reportsTable-view.fxml", "My Inprogress Reports");
        });
    }

    private void openAllSolvedReportsView() {
        allClosedCard.setOnMouseClicked((x) -> {
            loadReports(() -> operatorRestClient.getAllSolvedReports(), "report/reportsTable-view.fxml", "All Solved Reports");
        });
    }

    private void loadReports(Supplier<List<ReportDto>> reportSupplier, String viewPath, String label) {
        Stage waitingPopup = popupFactory.createWaitingPopup("Loading data...");
        waitingPopup.show();

        CompletableFuture
                .supplyAsync(reportSupplier::get)
                .thenAccept(reportDtoList -> {
                    Platform.runLater(() -> {
                        ReportCardManager.getInstance().setReportDtoList(reportDtoList);
                        waitingPopup.close();
                        try {
                            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(viewPath));
                            AnchorPane view = loader.load();
                            ReportViewController controller = loader.getController();
                            controller.setLabel(label);
                            viewManager.saveCurrentView(viewPath);
                            dashboardAnchorPane.getChildren().setAll(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }).exceptionally(ex -> {
                    Platform.runLater(waitingPopup::close);
                    return null;
                });
    }

    private void initializeLabels() {
        Task<HashMap<String, Integer>> task = new Task<>() {
            @Override
            protected HashMap<String, Integer> call() {
                return operatorRestClient.getDashboardData();
            }
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    dashboardData = getValue();
                    allNotAssignedLabel.setText(String.valueOf(dashboardData.get("allNotAssigned")));
                    allAssignedByUserLabel.setText(String.valueOf(dashboardData.get("allAssignedByUser")));
                    allClosedByUserLabel.setText(String.valueOf(dashboardData.get("allClosedByUser")));
                    allAssignedLabel.setText(String.valueOf(dashboardData.get("allAssigned")));
                    inProgressLabel.setText(String.valueOf(dashboardData.get("allInProgressByUser")));
                    allClosedLabel.setText(String.valueOf(dashboardData.get("allClosed")));
                    initializePieChart();
                });
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
            }
        };

        new Thread(task).start();
    }

    private Stage getStage() {
        return (Stage) this.dashboardAnchorPane.getScene().getWindow();
    }
}
