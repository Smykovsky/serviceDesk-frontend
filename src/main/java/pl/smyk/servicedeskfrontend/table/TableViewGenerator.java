package pl.smyk.servicedeskfrontend.table;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;

public class TableViewGenerator <T> {
    private final TableView<T> tableView;

    public TableViewGenerator() {
        tableView = new TableView<>();
    }

    public TableView<T> createTableView(ObservableList<T> data) {
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
        return tableView;
    }
}
