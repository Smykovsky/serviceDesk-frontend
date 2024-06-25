package pl.smyk.servicedeskfrontend.table;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.controller.ReportDetailsController;
import pl.smyk.servicedeskfrontend.dto.ReportDto;
import pl.smyk.servicedeskfrontend.manager.ViewManager;

import java.io.IOException;
import java.lang.reflect.Field;

public class TableViewGenerator <T> {
    private final TableView<T> tableView;

    public TableViewGenerator() {
        tableView = new TableView<>();
    }

    public TableView<T> createTableView(ObservableList<T> data, AnchorPane mainAnchorPane) {
        tableView.getColumns().clear();

        Class<?> c = data.get(0).getClass();

        for (Field field : c.getDeclaredFields()) {
            TableColumn<T, Object> column = new TableColumn<>(field.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableView.getColumns().forEach(col -> {
                col.setPrefWidth(TableView.USE_COMPUTED_SIZE);
            });
            tableView.getColumns().add(column);
        }

        tableView.setItems(data);

        tableView.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    T clickedRow = row.getItem();
                    System.out.println("Selected: " + clickedRow.toString());
                    openDetailView((ReportDto) clickedRow, mainAnchorPane);
                }
            });
            return row;
        });

        return tableView;
    }

    private void openDetailView(ReportDto report, AnchorPane mainAnchorPane) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("reportsView/reportDetails-view.fxml"));
            Parent root = loader.load();

            ReportDetailsController controller = loader.getController();
            controller.setReportDetails(report);

            mainAnchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
