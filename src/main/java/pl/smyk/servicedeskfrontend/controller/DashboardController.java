package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Pane notAssignedCard;

    @FXML
    private Pane myNotSolvedCard;

    @FXML
    private Pane mySolvedCard;

    @FXML
    private Pane allAssignedCard;

    @FXML
    private Pane myInProgressCard;

    @FXML
    private Pane allSolvedCard;

    @FXML
    private Button addButton;


    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    }

    private void openAllNotAssignedReportsView() {

    }

    private void openMyNotSolvedReportsView() {

    }

    private void openMySolvedReports() {

    }

    private void openAllAssignedReportsView() {
        allAssignedCard.setOnMouseClicked(x -> {
            System.out.println("assigned!");
        });
    }

    private void openMyInProgressReportsView() {

    }

    private void openAllSolvedReportsView() {

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
