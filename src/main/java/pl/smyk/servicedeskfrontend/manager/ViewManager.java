package pl.smyk.servicedeskfrontend.manager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.session.SessionManager;

import java.io.IOException;
import java.util.Optional;
import java.util.Stack;

public class ViewManager {
    private AnchorPane container;
    @Getter
    private final Stack<String> viewHistory;

    public ViewManager(AnchorPane container) {
        this.container = container;
        this.viewHistory = new Stack<>();
    }

    public void loadView(String fxmlFile) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
        try {
            AnchorPane newPane = fxmlLoader.load();
            saveCurrentView(fxmlFile);
            container.getChildren().setAll(newPane);
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

    public void reloadView(String fxmlFile) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
        try {
            Parent newPane = fxmlLoader.load();
            container.getChildren().setAll(newPane);
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
            saveCurrentView(fxmlFile);
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

    public void goBack() {
        if (!SessionManager.getInstance().getViewHistory().isEmpty()) {
            SessionManager.getInstance().getViewHistory().pop();
            String previousView = SessionManager.getInstance().getViewHistory().peek();
            System.out.println("previousView: " + previousView);
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(previousView));
            try {
                Parent previousPane = fxmlLoader.load();
                container.getChildren().clear();
                container.getChildren().add(previousPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveCurrentView(String currentFxmlFile) {
        SessionManager.getInstance().getViewHistory().push(currentFxmlFile);
        System.out.println(SessionManager.getInstance().getViewHistory());
    }
}
