package pl.smyk.servicedeskfrontend.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import pl.smyk.servicedeskfrontend.MainApp;
import pl.smyk.servicedeskfrontend.dto.ArticleDto;
import pl.smyk.servicedeskfrontend.manager.ViewManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ArticleViewController implements Initializable {
    @FXML
    private AnchorPane articleAnchorPane;
    private Long id;
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
    @FXML
    private Button updateButton;
    @FXML
    private Button commentButton;
    private ViewManager viewManager;
    private String pathToFile;
    private int currentPageIndex = 0;

    public void initData(ArticleDto article) {
        this.id = article.getId();
        titleLabel.setText(article.getTitle());
        descriptionLabel.setText(article.getDescription());
        createdByLabel.setText(article.getCreatedBy());
        updatedByLabel.setText((article.getUpdatedBy() != null ? article.getUpdatedBy() : "Brak aktualizacji"));
        attachmentLabel.setText("Attachments: ");
        pathToFile = article.getAttachmentPath();
        if (article.getAttachmentPath() != null) {
            pdfButton.setText("PDF");
        } else pdfButton.setVisible(false);
        commentsLabel.setText("Comments: ");

        for (String comment : article.getComments()) {
            Label userLabel = new Label("x");
            Label commentLabel = new Label(comment);
            commentsBox.setSpacing(2);
            Insets insets = new Insets(10);
            commentsBox.setPadding(insets);
            commentsBox.getChildren().addAll(userLabel, commentLabel);

            userLabel.getStyleClass().add("userLabel");
            commentLabel.getStyleClass().add("commentLabel");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewManager = new ViewManager(articleAnchorPane);
        initializePdfButton();
        initializeUpdateButton();
        initializeCommentButton();
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

        Label prevButton = new Label("<- Previous");
        prevButton.setVisible(false);
        Label nextButton = new Label("Next ->");
        prevButton.setStyle("-fx-text-fill: white; -fx-background-color: red; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 5; -fx-font-size: 14px; -fx-font-weight: bold");
        prevButton.setOnMouseClicked(event -> {
            if (currentPageIndex > 0) {
                currentPageIndex--;
                imageView.setImage(images[currentPageIndex]);
                prevButton.setVisible(currentPageIndex > 0);
                nextButton.setVisible(currentPageIndex < numPages - 1);
            }
        });


        nextButton.setStyle("-fx-text-fill: white; -fx-background-color: green; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 5;-fx-font-size: 14px; -fx-font-weight: bold");
        nextButton.setOnMouseClicked(event -> {
            if (currentPageIndex < numPages - 1) {
                currentPageIndex++;
                imageView.setImage(images[currentPageIndex]);
                prevButton.setVisible(currentPageIndex > 0);
                nextButton.setVisible(currentPageIndex < numPages - 1);
            }
        });

        Insets insets = new Insets(10);
        stackPane.getChildren().addAll(prevButton, nextButton);

        StackPane.setMargin(prevButton, insets);
        StackPane.setMargin(nextButton, insets);
        StackPane.setAlignment(prevButton, Pos.TOP_LEFT);
        StackPane.setAlignment(nextButton, Pos.TOP_RIGHT);
    }

    private void initializeUpdateButton() {
        updateButton.setOnAction(x -> {
            try {
                FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("article/updateArticle-view.fxml"));
                AnchorPane newPane = loader.load();
                Scene scene = new Scene(newPane);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);

                UpdateArticleController controller = loader.getController();
                controller.setId(id, titleLabel.getText(), descriptionLabel.getText());

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initializeCommentButton() {
        commentButton.setOnAction(x -> {
            try {
                FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("article/addComment-view.fxml"));
                AnchorPane newPane = loader.load();
                Scene scene = new Scene(newPane);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);


                AddCommentController controller = loader.getController();
                controller.setArticleData(id);

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
