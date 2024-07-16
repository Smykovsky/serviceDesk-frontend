package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import pl.smyk.servicedeskfrontend.dto.ArticleDto;
import pl.smyk.servicedeskfrontend.factory.PopupFactory;
import pl.smyk.servicedeskfrontend.manager.ViewManager;
import pl.smyk.servicedeskfrontend.rest.OperatorRestClient;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateArticleController implements Initializable {
    @FXML
    private AnchorPane addArticleAnchorPane;
    @FXML
    private Button updateButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField titleTextField;
    private Long articleId;
    private final PopupFactory popupFactory;
    private final OperatorRestClient operatorRestClient;
    private ViewManager viewManager;

    public UpdateArticleController() {
        popupFactory = new PopupFactory();
        operatorRestClient = new OperatorRestClient();
    }

    public void setId(Long id, String title, String description) {
        this.articleId = id;
        titleTextField.setText(title);
        descriptionTextArea.setText(description);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewManager = new ViewManager(addArticleAnchorPane);
        initializeUpdateButton();
        initializeCloseButton();
    }

    private void initializeUpdateButton() {
        updateButton.setOnAction(x -> {
            Thread thread = new Thread(() -> {
                ArticleDto articleDto = new ArticleDto();
                articleDto.setId(articleId);
                articleDto.setTitle(titleTextField.getText());
                articleDto.setDescription(descriptionTextArea.getText());
                ResponseEntity<?> responseEntity = operatorRestClient.updateArticle(articleDto);
                Platform.runLater(() -> {
                    getStage().close();
                    popupFactory.createInfoPopup(responseEntity.getBody().toString());
                });
            });
            thread.start();
        });
    }

    private void initializeCloseButton() {
        cancelButton.setOnAction(x -> {
            getStage().close();
        });
    }

    private Stage getStage() {
        return (Stage) addArticleAnchorPane.getScene().getWindow();
    }

}
