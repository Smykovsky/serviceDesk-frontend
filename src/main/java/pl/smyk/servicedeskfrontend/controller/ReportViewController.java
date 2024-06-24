package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import pl.smyk.servicedeskfrontend.dto.ReportDto;
import pl.smyk.servicedeskfrontend.rest.ReportRestClient;
import pl.smyk.servicedeskfrontend.table.TableViewGenerator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReportViewController implements Initializable {
    private final ReportRestClient reportRestClient;

    @FXML
    private Pagination pagination;
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

//        pagination.setPageFactory(new Callback<Integer, Node>() {
//            @Override
//            public javafx.scene.Node call(Integer pageIndex) {
//                return createPage(pageIndex);
//            }
//        });
    }

    private TableView<ReportDto> createPage(int pageIndex) {
        TableView<ReportDto> tableView = tableViewGenerator.createTableView(reportObservableList);
        int start = pageIndex * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, reportObservableList.size());
        tableView.setItems(FXCollections.observableArrayList(reportObservableList.subList(start, end)));

        return tableView;

//        TableView<Report> table = new TableView<>();
//
//        TableColumn<Report, Integer> idColumn = new TableColumn<>("ID");
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//        idColumn.setPrefWidth(50);
//
//        TableColumn<Report, String> tytulColumn = new TableColumn<>("Tytuł");
//        tytulColumn.setCellValueFactory(new PropertyValueFactory<>("tytul"));
//        tytulColumn.setPrefWidth(200);
//
//        TableColumn<Report, String> opisColumn = new TableColumn<>("Opis");
//        opisColumn.setCellValueFactory(new PropertyValueFactory<>("opis"));
//        opisColumn.setPrefWidth(300);
//
//        TableColumn<Report, String> statusColumn = new TableColumn<>("Status");
//        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
//        statusColumn.setPrefWidth(100);
//
//        TableColumn<Report, String> dataColumn = new TableColumn<>("Data");
//        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
//        dataColumn.setPrefWidth(150);
//
//        table.getColumns().addAll(idColumn, tytulColumn, opisColumn, statusColumn, dataColumn);
//
//        int start = pageIndex * ROWS_PER_PAGE;
//        int end = Math.min(start + ROWS_PER_PAGE, reportObservableList.size());
//        table.setItems(FXCollections.observableArrayList(reportObservableList.subList(start, end)));
//
//        return table;
    }

}
