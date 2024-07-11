package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.smyk.servicedeskfrontend.dto.ArticleDto;
import pl.smyk.servicedeskfrontend.rest.OperatorRestClient;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
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
                    new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"),
                    new FileChooser.ExtensionFilter("Pliki obrazu", "*.png", "*.jpg", "*.gif")
            );

            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(addArticleAnchorPane.getScene().getWindow());
            if (selectedFiles != null) {
                for (File file : selectedFiles) {
                    fileNameLabel.setText(file.getName());
                }
            }
        });
    }

    private void initializeAddArticleButton() {
        addArticleButton.setOnAction(x -> {
            Thread thread = new Thread(() -> {
                ArticleDto dto = new ArticleDto();
                dto.setTitle(titleTextField.getText());
                dto.setDescription(descriptionTextArea.getText());
                dto.setAttachmentPath(fileNameLabel.getText());
                operatorRestClient.addArticle(dto);
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
