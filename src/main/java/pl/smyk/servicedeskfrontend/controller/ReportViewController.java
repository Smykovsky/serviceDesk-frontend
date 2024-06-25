package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import pl.smyk.servicedeskfrontend.dto.ReportDto;
import pl.smyk.servicedeskfrontend.rest.ReportRestClient;
import pl.smyk.servicedeskfrontend.table.TableViewGenerator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ReportViewController implements Initializable {
    private final ReportRestClient reportRestClient;

    @FXML
    private Pagination pagination;

    @FXML
    private AnchorPane mainAnchorPane;
    private static final int ROWS_PER_PAGE = 24;
    private ObservableList<ReportDto> reportObservableList;
    private TableViewGenerator<ReportDto> tableViewGenerator;

    public ReportViewController() {
        reportRestClient = new ReportRestClient();
        tableViewGenerator = new TableViewGenerator<>();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread thread = new Thread(() -> {
            List<ReportDto> allNotAssignedReports = reportRestClient.getAllNotAssignedReports();
            reportObservableList = FXCollections.observableArrayList(allNotAssignedReports);
            Platform.runLater(() -> {
                pagination.setPageFactory(this::createPage);
                int totalPages = (int) Math.ceil(reportObservableList.size() * 1.0 / ROWS_PER_PAGE);
                pagination.setPageCount(totalPages);
            });
        });
        thread.start();
    }

    private TableView<ReportDto> createPage(int pageIndex) {
        TableView<ReportDto> tableView = tableViewGenerator.createTableView(reportObservableList, this.mainAnchorPane);
        int start = pageIndex * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, reportObservableList.size());
        tableView.setItems(FXCollections.observableArrayList(reportObservableList.subList(start, end)));

        return tableView;
    }
}
