package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.dto.ArticleRequest;
import pl.smyk.servicedeskfrontend.manager.FileService;
import pl.smyk.servicedeskfrontend.rest.OperatorRestClient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddArticleController implements Initializable {
    @FXML
    private AnchorPane addArticleAnchorPane;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button fileButton;
    @FXML
    private Label fileNameLabel;

    @FXML
    private Button addArticleButton;
    @FXML
    private Button cancelButton;

    private File selectedFile;
    private final OperatorRestClient operatorRestClient;

    public AddArticleController() {
        operatorRestClient = new OperatorRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeFileButton();
        initializeAddArticleButton();
        initializeCancelButton();
    }

    private void initializeFileButton() {
        fileButton.setOnAction(x -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz plik");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Pliki PDF", "*.pdf"),
                    new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"),
                    new FileChooser.ExtensionFilter("Pliki obrazu", "*.png", "*.jpg", "*.gif")
            );

            selectedFile = fileChooser.showOpenDialog(addArticleAnchorPane.getScene().getWindow());
            if (selectedFile != null) {
                fileNameLabel.setText(selectedFile.getName());
            }
        });
    }

    private void initializeAddArticleButton() {
        addArticleButton.setOnAction(x -> {
            Thread thread = new Thread(() -> {
                ArticleRequest articleRequest = new ArticleRequest();
                articleRequest.setTitle(titleTextField.getText());
                articleRequest.setDescription(descriptionTextArea.getText());

                if (selectedFile != null) {
                    FileSystemResource fileResource = new FileSystemResource(selectedFile);
                    articleRequest.setFile(fileResource);
                }

                operatorRestClient.addArticle(articleRequest);
                Platform.runLater(() -> {
                    getStage().close();
                });
            });
            thread.start();
        });
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(x -> {
            getStage().close();
        });
    }

    private Stage getStage() {
        return (Stage) addArticleAnchorPane.getScene().getWindow();
    }
}
