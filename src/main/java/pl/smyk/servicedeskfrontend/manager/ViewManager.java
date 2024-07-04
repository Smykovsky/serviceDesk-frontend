package pl.smyk.servicedeskfrontend.manager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.smyk.servicedeskfrontend.MainApp;

import java.io.IOException;
import java.util.Optional;

public class ViewManager {
    private AnchorPane container;

    public ViewManager(AnchorPane container) {
        this.container = container;
    }

    public void loadView(String fxmlFile) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
        try {
            AnchorPane newPane = fxmlLoader.load();
            container.getChildren().clear();
            container.getChildren().add(newPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadScene(String fxmlFile) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
        try {
            Stage stage = new Stage();
            Stage primaryStage = (Stage) container.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
            stage.setScene(scene);
            stage.setTitle("Service Desk");
            primaryStage.close();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadNewWindow(String fxmlFile) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
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
    }

    public void loadAuthScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("auth-view.fxml"));
        try {
            Stage stage = new Stage();
            Stage primaryStage = (Stage) container.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Service Desk");
            stage.setScene(scene);
            primaryStage.close();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
