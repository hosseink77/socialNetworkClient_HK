package client.controllers;

import client.ClientMain;
import client.model.CreateRestTemplate;
import client.model.entity.PostEntity;
import client.model.entity.UserEntity;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReadPostScreen implements Initializable {
    @FXML
    private AnchorPane anchorPane;

//    @FXML
//    private AnchorPane homeScreenAnchorPane;

    @FXML
    private ScrollPane postScroll_pane;

    @FXML
    private Label postTitle_text;

    @FXML
    private Text post_text;

    @FXML
    private ImageView imgPostSmall;

//    @FXML
//    private JFXButton authorName_button;

    @FXML
    private JFXButton authorUsername_button;

//    @FXML
//    private JFXButton authorUuid_button;

    @FXML
    private JFXButton deletePostButton;

    @FXML
    private JFXButton editPostButton;


    @FXML
    private Label dateCreated_text;

    @FXML
    private Label tags_text;
    @FXML
    private Labeled errorText;

    private String postOwnerId;
    private String postTitle;
    private UserEntity clientUserObject;
    private static String backPage;

    public static void setPostDataObject(PostEntity postDataObject) {
        ReadPostScreen.postDataObject = postDataObject;
    }

    private static PostEntity postDataObject;

    @FXML
    private JFXToggleButton orientationTxtArea;

    @FXML
    private JFXToggleButton btnShowLarge;

    @FXML
    private StackPane stackPane;


    @FXML
    private ImageView imgPostLarge;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setPostData(postDataObject);

        setClientUser(LoginController.getUserEntity());
    }

//    public void setHomeScreenAnchorPane(AnchorPane homeScreenAnchorPane) {
//        this.homeScreenAnchorPane = homeScreenAnchorPane;
//    }

    public void setPostOwner(String postOwnerId) {
        this.postOwnerId = postOwnerId;

        this.authorUsername_button.setText(postOwnerId);

    }

    public void setClientUser(UserEntity clientUser) {
        this.clientUserObject = clientUser;

        if (clientUser.getUserName().equals(postOwnerId)) {
            deletePostButton.setVisible(true);
            deletePostButton.setDisable(false);
            editPostButton.setVisible(true);
            editPostButton.setDisable(false);
        }
    }

    public static void setBackPage(String pageName){
        backPage = pageName;
    }

    public void setPostData(PostEntity postData) {
        postScroll_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

//        this.postDataObject = postData;
        postOwnerId = postData.getOwnerId();
        this.authorUsername_button.setText(postOwnerId);
        this.postTitle = postData.getTitle();

        this.postTitle_text.setText(postData.getTitle());
        this.dateCreated_text.setText(ClientMain.formatDate(postData.getDate()));
        this.tags_text.setText(postData.getTags());
        this.post_text.setText(postData.getPostText());
        if(postData.getImage() != null) {

            ByteArrayInputStream bis = new ByteArrayInputStream(postData.getImage());
            BufferedImage bImage2 = null;

            try {
                bImage2 = ImageIO.read(bis);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Image image = SwingFXUtils.toFXImage(bImage2, null);
            this.imgPostSmall.setImage(image);
            this.imgPostLarge.setImage(image);
//            imgPostLarge.fitHeightProperty().bind(postScroll_pane.heightProperty());
//            imgPostLarge.fitWidthProperty().bind(postScroll_pane.widthProperty());
            imgPostSmall.setVisible(true);
            btnShowLarge.setVisible(true);
        }
    }


    public void handleAuthorClicked(MouseEvent mouseEvent) {
        System.out.println("Author buttons clicked!");
        UserEntity userAuthor = CreateRestTemplate.buildGet("", UserEntity.class, postDataObject.getOwnerId());
        ProfileScreenController profileScreen = (ProfileScreenController) ClientMain.loadUI("ProfileScreen", anchorPane);
        profileScreen.setupUser(clientUserObject , userAuthor);

    }

    public void handleBackButtonClick(MouseEvent mouseEvent) {
        imgPostSmall.setImage(null);
        imgPostLarge.setImage(null);
        if(backPage.equals("HomeScreen")) {
            ClientMain.loadUI("HomeScreen", anchorPane);
        }else{
            ProfileScreenController instance = ProfileScreenController.getInstance();
            ProfileScreenController profileScreen = (ProfileScreenController) ClientMain.loadUI("ProfileScreen", anchorPane);
            profileScreen.setupUser(ProfileScreenController.getInstance().getClientUserObject(),ProfileScreenController.getInstance().getUserObject());
        }
    }

    public void onDeleteClicked(ActionEvent actionEvent) {
        if (CreateRestTemplate.isConnected()) {
            if (clientUserObject.getUserName().equals(postOwnerId)) {

                System.out.println("Delete post button was clicked!");
                CreateRestTemplate.buildDeleteByPath("post/delete/" + postOwnerId + "/" + postTitle);

                errorText.setTextFill(Paint.valueOf(Color.GREEN.toString()));
                errorText.setText("Post deleted successfully!");
                errorText.setVisible(true);

            }
        } else {
            errorText.setTextFill(Paint.valueOf(new Color(1, 0.2, 0.2, 1).toString()));
            errorText.setText("Error processing request! Please try again later.");
            errorText.setVisible(true);
        }
    }

    public void actionToggleButton(ActionEvent actionEvent) {
        if (orientationTxtArea.isSelected()) post_text.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        else post_text.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }

    public void actionBtnShowLarge(ActionEvent actionEvent) {

        if (btnShowLarge.isSelected()) {
            imgPostLarge.setVisible(true);
        } else {
            imgPostLarge.setVisible(false);

        }
    }


    public void onEditClicked(ActionEvent actionEvent) {
        EditPostController.setPost(postDataObject);
        ClientMain.loadUI("EditPostScreen",anchorPane);
    }
}
