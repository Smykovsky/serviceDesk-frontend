package pl.smyk.servicedeskfrontend.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import pl.smyk.servicedeskfrontend.dto.CommentRequest;
import pl.smyk.servicedeskfrontend.factory.PopupFactory;
import pl.smyk.servicedeskfrontend.rest.UserReportRestClient;
import pl.smyk.servicedeskfrontend.session.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class AddCommentController implements Initializable {
    @FXML
    private AnchorPane addCommentAnchorPane;
    @FXML
    private Button addButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextArea commentTextArea;
    private Long articleId;

    private final UserReportRestClient userReportRestClient;
    private final PopupFactory popupFactory;

    public AddCommentController() {
        userReportRestClient = new UserReportRestClient();
        popupFactory = new PopupFactory();
    }

    public void setArticleData(Long id) {
        this.articleId = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!SessionManager.getInstance().getUserRoles().contains("OPERATOR")) {
            addButton.setVisible(false);
        }
        initializeAddButton();
        initializeCloseButton();
        commentTextArea.setWrapText(true);
    }

    private void initializeAddButton() {
        addButton.setOnAction(x -> {
            Thread thread = new Thread(() -> {
                CommentRequest request = new CommentRequest();
                request.setArticleId(articleId);
                request.setComment(commentTextArea.getText());
                ResponseEntity<?> responseEntity = userReportRestClient.commentArticle(request);
                Platform.runLater(() -> {
                    Stage infoPopup = popupFactory.createInfoPopup(responseEntity.getBody().toString());
                    getStage().close();
                    infoPopup.show();
                });
            });
            thread.start();
        });
    }

    private void initializeCloseButton() {
        closeButton.setOnAction(x -> {
            getStage().close();
        });
    }

    private Stage getStage() {
        return (Stage) addCommentAnchorPane.getScene().getWindow();
    }
}
