package pl.smyk.servicedeskfrontend.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import pl.smyk.servicedeskfrontend.dto.ArticleDto;
import pl.smyk.servicedeskfrontend.manager.ViewManager;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ArticleViewController implements Initializable {
    @FXML
    private AnchorPane articleAnchorPane;
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
    private Button pdfButton;
    @FXML
    private Label commentsLabel;
    @FXML
    private VBox commentsBox;
    private ViewManager viewManager;
    private String pathToFile;

    public void initData(ArticleDto article) {
        titleLabel.setText("Title: " + article.getTitle());
        descriptionLabel.setText("Description: " + article.getDescription());
        createdByLabel.setText("Created by: " + article.getCreatedBy());
        updatedByLabel.setText("Updated by: " + (article.getUpdatedBy() != null ? article.getUpdatedBy() : "Brak aktualizacji"));
        attachmentLabel.setText("Attachments: ");
        pathToFile = article.getAttachmentPath();
        if (article.getAttachmentPath() != null) {
            pdfButton.setText("PDF");
        } else pdfButton.setVisible(false);
        commentsLabel.setText("Comments: ");

        for (String comment : article.getComments()) {
            Label commentLabel = new Label(comment);
            commentsBox.getChildren().add(commentLabel);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewManager = new ViewManager(articleAnchorPane);
        initializePdfButton();
    }

    private void initializePdfButton() {
        pdfButton.setOnAction(x -> {
            File file = new File(pathToFile);
            try {
                PDDocument pdDocument = PDDocument.load(file);
                int numPages = pdDocument.getNumberOfPages();
                PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);

                WritableImage[] pages = new WritableImage[numPages];
                for (int i = 0; i < numPages; i++) {
                    BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, 60);
                    WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                    pages[i] = fxImage;
                }

                showPdfImageInNewWindow(pages);

                pdDocument.close();
            } catch (IOException e) {
                throw new RuntimeException("Failed to open PDF file.", e);
            }
        });
    }

    private void showPdfImageInNewWindow(WritableImage[] images) {
        Stage pdfStage = new Stage();
        pdfStage.setTitle("PDF Viewer");

        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView(images[0]);
        stackPane.getChildren().add(imageView);

        pdfStage.setScene(new Scene(stackPane, 700, 750));
        pdfStage.show();

        int numPages = images.length;
        final int[] currentPageIndex = {0};

        Label prevButton = new Label("<- Previous");
        prevButton.setStyle("-fx-text-fill: red; -fx-padding: 10; -fx-font-size: 14px");
        prevButton.setOnMouseClicked(event -> {
            if (currentPageIndex[0] > 0) {
                currentPageIndex[0]--;
                imageView.setImage(images[currentPageIndex[0]]);
            }
        });

        Label nextButton = new Label("Next ->");
        nextButton.setStyle("-fx-text-fill: green; -fx-padding: 10;-fx-font-size: 14px");
        nextButton.setOnMouseClicked(event -> {
            if (currentPageIndex[0] < numPages - 1) {
                currentPageIndex[0]++;
                imageView.setImage(images[currentPageIndex[0]]);
            }
        });

        VBox buttonBox = new VBox( prevButton, nextButton);
        stackPane.getChildren().add(buttonBox);

        StackPane.setAlignment(buttonBox, javafx.geometry.Pos.BOTTOM_CENTER);
    }
}
