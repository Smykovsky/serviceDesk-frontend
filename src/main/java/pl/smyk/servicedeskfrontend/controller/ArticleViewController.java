package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pl.smyk.servicedeskfrontend.dto.ArticleDto;

import java.net.URL;
import java.util.ResourceBundle;

public class ArticleViewController implements Initializable {
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label createdByLabel;
    @FXML
    private Label updatedByLabel;
    @FXML
    private Label attachmentLabel;
    @FXML
    private Label commentsLabel;
    @FXML
    private VBox commentsBox;

    public void initData(ArticleDto article) {
        titleLabel.setText("Title: " + article.getTitle());
        descriptionLabel.setText("Description: " + article.getDescription());
        createdByLabel.setText("Created by: " + article.getCreatedBy());
        updatedByLabel.setText("Updated by: " + (article.getUpdatedBy() != null ? article.getUpdatedBy() : "Brak aktualizacji"));
        attachmentLabel.setText("Attachments: ");
        commentsLabel.setText("Comments: ");

        for (String comment : article.getComments()) {
            Label commentLabel = new Label(comment);
            commentsBox.getChildren().add(commentLabel);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
