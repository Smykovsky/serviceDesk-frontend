package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private LineChart<?, ?> lineChart;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
    private ViewManager viewManager;
    private HashMap<String, Integer> dashboardData;
    private OperatorRestClient operatorRestClient;
    private PopupFactory popupFactory;

    public OperatorDashboardController() {
        operatorRestClient = new OperatorRestClient();
        popupFactory = new PopupFactory();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewManager = new ViewManager(this.dashboardAnchorPane);

        initializeLabels();
        initializeLineChart();
    }


    private void initializeLineChart() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Solve reports");

        series.getData().add(new XYChart.Data("2024-01-01", 23));
        series.getData().add(new XYChart.Data("2024-02-01", 11));
        series.getData().add(new XYChart.Data("2024-03-01", 13));
        series.getData().add(new XYChart.Data("2024-04-01", 5));

        lineChart.getData().addAll(series);
        openAllNotAssignedReportsView();
        openMyNotClosedReportsView();
        openMyClosedReportsView();
        openAllAssignedReportsView();
        openMyInProgressReportsView();
        openAllSolvedReportsView();
    }

    private void openAllNotAssignedReportsView() {
        allNotAssignedCard.setOnMouseClicked((x) -> {
            loadReports(() -> operatorRestClient.getAllNotAssignedReports(), "reportsView/reportsTable-view.fxml");
        });
    }

    private void openMyNotClosedReportsView() {
        myNotClosedCard.setOnMouseClicked((x) -> {
            loadReports(() -> operatorRestClient.getMyNotClosedReports(), "reportsView/reportsTable-view.fxml");
        });
    }

    private void openMyClosedReportsView() {
        myClosedCard.setOnMouseClicked((x) -> {
            loadReports(() -> operatorRestClient.getMyAllClosedReports(), "reportsView/reportsTable-view.fxml");
        });
    }

    private void openAllAssignedReportsView() {
        allAssignedCard.setOnMouseClicked(x -> {
            loadReports(() -> operatorRestClient.getAllAssignedReports(), "reportsView/reportsTable-view.fxml");
        });
    }

    private void openMyInProgressReportsView() {
        myInProgressCard.setOnMouseClicked((x) -> {
            loadReports(() -> operatorRestClient.getMyInProgressReports(), "reportsView/reportsTable-view.fxml");
        });
    }

    private void openAllSolvedReportsView() {
        allClosedCard.setOnMouseClicked((x) -> {
            loadReports(() -> operatorRestClient.getAllSolvedReports(), "reportsView/reportsTable-view.fxml");
        });
    }

    private void loadReports(Supplier<List<ReportDto>> reportSupplier, String viewPath) {
        Stage waitingPopup = popupFactory.createWaitingPopup("Loading data...");
        waitingPopup.show();

        CompletableFuture
            .supplyAsync(reportSupplier::get)
            .thenAccept(reportDtoList -> {
                Platform.runLater(() -> {
                    ReportCardManager.getInstance().setReportDtoList(reportDtoList);
                    waitingPopup.close();
                    viewManager.loadView(viewPath);
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
                    HashMap<String, Integer> data = getValue();
                    allNotAssignedLabel.setText(String.valueOf(data.get("allNotAssigned")));
                    allAssignedByUserLabel.setText(String.valueOf(data.get("allAssignedByUser")));
                    allClosedByUserLabel.setText(String.valueOf(data.get("allClosedByUser")));
                    allAssignedLabel.setText(String.valueOf(data.get("allAssigned")));
                    inProgressLabel.setText(String.valueOf(data.get("allInProgressByUser")));
                    allClosedLabel.setText(String.valueOf(data.get("allClosed")));
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
