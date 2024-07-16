package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
import pl.smyk.servicedeskfrontend.rest.UserReportRestClient;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class UserDashboardController implements Initializable {
    @FXML
    private AnchorPane userDashboardAnchorPane;
    @FXML
    private Pane myAssignedCard;
    @FXML
    private Label myAssignedLabel;
    @FXML
    private Pane myNotAssignedCard;
    @FXML
    private Label myNotAssignedLabel;
    @FXML
    private Pane myClosedCard;
    @FXML
    private Label myClosedLabel;
    @FXML
    private Button addButton;
    private ScheduledExecutorService scheduledExecutorService;

    private final UserReportRestClient userReportRestClient;
    private final PopupFactory popupFactory;
    private ViewManager viewManager;

    public UserDashboardController() {
        userReportRestClient = new UserReportRestClient();
        popupFactory = new PopupFactory();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewManager = new ViewManager(this.userDashboardAnchorPane);

        initializeLabels();
        initializeAddButton();
        openMyAllNotAssignedCard();
        openMyAllAssignedCard();
        openMyAllClosedReports();

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::initializeLabels, 0, 60, TimeUnit.SECONDS);
    }

    private void openMyAllNotAssignedCard() {
        myNotAssignedCard.setOnMouseClicked((x) -> {
            loadReports(userReportRestClient::getMyNotAssignedReports, "report/reportsTable-view.fxml");
        });
    }

    private void openMyAllAssignedCard() {
        myAssignedCard.setOnMouseClicked((x) -> {
            loadReports(userReportRestClient::getMyAssignedReports, "report/reportsTable-view.fxml");
        });
    }

    private void openMyAllClosedReports() {
        myClosedCard.setOnMouseClicked((x) -> {
            loadReports(userReportRestClient::getMyClosedReports, "report/reportsTable-view.fxml");
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

    public void initializeLabels() {
        Task<HashMap<String, Integer>> task = new Task<>() {
            @Override
            protected HashMap<String, Integer> call() {
                return userReportRestClient.getDashboardData();
            }
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    HashMap<String, Integer> data = getValue();
                    myNotAssignedLabel.setText(String.valueOf(data.get("myNotAssigned")));
                    myAssignedLabel.setText(String.valueOf(data.get("myAssigned")));
                    myClosedLabel.setText(String.valueOf(data.get("myClosed")));
                });
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
            }
        };

        new Thread(task).start();
    }

    private void initializeAddButton() {
        addButton.setOnAction((x) -> {
            viewManager.loadNewWindow("report/addReportForm-view.fxml");
        });
    }
}
