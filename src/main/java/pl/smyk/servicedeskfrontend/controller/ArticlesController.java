package pl.smyk.servicedeskfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.dto.ArticleDto;
import pl.smyk.servicedeskfrontend.manager.ViewManager;
import pl.smyk.servicedeskfrontend.rest.ArticleRestClient;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ArticlesController implements Initializable {
    @FXML
    private AnchorPane articlesAnchorPane;
    @FXML
    private ScrollPane contentScrollPane;
    @FXML
    private Button addButton;

    private final ArticleRestClient articleRestClient;
    private ViewManager viewManager;

    public ArticlesController() {
        articleRestClient = new ArticleRestClient();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewManager = new ViewManager(articlesAnchorPane);
        initializeAddButton();
        List<ArticleDto> articles = fetchArticles();
        VBox content = new VBox();
        content.getStyleClass().add("content-hbox");
        content.setSpacing(15);

        for (ArticleDto article : articles) {
            AnchorPane articleCard = null;
            try {
                articleCard = createArticleCard(article);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (articleCard != null) {
                content.getChildren().add(articleCard);
            }
        }

        contentScrollPane.setContent(content);
    }

    private void initializeAddButton() {
        addButton.setOnAction(x -> {
            viewManager.loadNewWindow("addArticle-view.fxml");
        });
    }

    private List<ArticleDto> fetchArticles() {
        return articleRestClient.getAllArticles();
    }

    private AnchorPane createArticleCard(ArticleDto article) throws IOException {
        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("article-card");

        Label titleLabel = new Label("Title: " + article.getTitle());
        titleLabel.getStyleClass().add("article-title");

        Label descriptionLabel = new Label("Description: " + article.getDescription());
        descriptionLabel.getStyleClass().add("article-description");

        Label createdByLabel = new Label("Created by: " + article.getCreatedBy());
        createdByLabel.getStyleClass().add("article-author");

        Label updatedByLabel = new Label("Updated by: " + (article.getUpdatedBy() != null ? article.getUpdatedBy() : "Brak aktualizacji"));
        updatedByLabel.getStyleClass().add("article-author");

        Label comments = new Label("Comments");
        comments.getStyleClass().add("article-author");

        AnchorPane.setTopAnchor(titleLabel, 10.0);
        AnchorPane.setLeftAnchor(titleLabel, 10.0);

        AnchorPane.setTopAnchor(descriptionLabel, 40.0);
        AnchorPane.setLeftAnchor(descriptionLabel, 10.0);

        AnchorPane.setTopAnchor(createdByLabel, 70.0);
        AnchorPane.setLeftAnchor(createdByLabel, 10.0);

        AnchorPane.setTopAnchor(updatedByLabel, 85.0);
        AnchorPane.setLeftAnchor(updatedByLabel, 10.0);

        card.getChildren().addAll(titleLabel, descriptionLabel, createdByLabel, updatedByLabel);
        card.setOnMouseClicked(event -> {
            System.out.println("Clicked article: " + article.getTitle() + article.getId());
            openArticleView(article);
        });
        return card;
    }

    private void openArticleView(ArticleDto article) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("article-view.fxml"));
            Parent root = loader.load();

            ArticleViewController controller = loader.getController();
            controller.initData(article);

            articlesAnchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
