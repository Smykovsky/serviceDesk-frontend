package pl.smyk.servicedeskfrontend.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.dto.ReportDto;
import pl.smyk.servicedeskfrontend.manager.ReportCardManager;
import pl.smyk.servicedeskfrontend.manager.ViewManager;
import pl.smyk.servicedeskfrontend.rest.ReportRestClient;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
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
    private Button addButton;


    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;
    private ViewManager viewManager;
    private HashMap<String, Integer> dashboardData;

    private ReportRestClient reportRestClient;

    public DashboardController () {
        reportRestClient = new ReportRestClient();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread thread = new Thread(() -> {
            dashboardData = reportRestClient.getDashboardData();
            System.out.println(dashboardData);
        });

        thread.start();
        viewManager = new ViewManager(this.dashboardAnchorPane);
        initializeAddButton();
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
            List<ReportDto> reportDtoList = reportRestClient.getAllNotAssignedReports();
            ReportCardManager.getInstance().setReportDtoList(reportDtoList);
            viewManager.loadView("reportsView/allNotAssignedReports-view.fxml");
        });
    }

    private void openMyNotClosedReportsView() {
        myNotClosedCard.setOnMouseClicked((x) -> {
            List<ReportDto> reportDtoList = reportRestClient.getMyNotClosedReports();
            ReportCardManager.getInstance().setReportDtoList(reportDtoList);
            viewManager.loadView("reportsView/myNotClosedReports-view.fxml");
        });
    }

    private void openMyClosedReportsView() {
        myClosedCard.setOnMouseClicked((x) -> {
            List<ReportDto> reportDtoList = reportRestClient.getMyAllClosedReports();
            ReportCardManager.getInstance().setReportDtoList(reportDtoList);
            viewManager.loadView("reportsView/myClosedReports-view.fxml");
        });
    }

    private void openAllAssignedReportsView() {
        allAssignedCard.setOnMouseClicked(x -> {
            List<ReportDto> reportDtoList = reportRestClient.getAllAssignedReports();
            ReportCardManager.getInstance().setReportDtoList(reportDtoList);
            viewManager.loadView("reportsView/allAssignedReports-view.fxml");
        });
    }

    private void openMyInProgressReportsView() {
        myInProgressCard.setOnMouseClicked((x) -> {
            List<ReportDto> reportDtoList = reportRestClient.getMyInProgressReports();
            ReportCardManager.getInstance().setReportDtoList(reportDtoList);
            viewManager.loadView("reportsView/myInProgressReports-view.fxml");
        });
    }

    private void openAllSolvedReportsView() {
        allClosedCard.setOnMouseClicked((x) -> {
            List<ReportDto> reportDtoList = reportRestClient.getMyAllClosedReports();
            ReportCardManager.getInstance().setReportDtoList(reportDtoList);
            viewManager.loadView("reportsView/myClosedReports-view.fxml");
        });
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

    private Stage getStage() {
        return (Stage) this.dashboardAnchorPane.getScene().getWindow();
    }
}
