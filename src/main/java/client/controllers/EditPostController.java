package client.controllers;

import client.ClientMain;

import client.model.FileHelper;
import client.model.CreateRestTemplate;
import client.model.entity.PostEntity;
import client.model.entity.UserEntity;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditPostController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField titleText;

    @FXML
    private TextField tagsText;

    @FXML
    private TextArea postText;

    @FXML
    private Label missingEntryText;

    @FXML
    JFXToggleButton orientationTxtArea;

    @FXML
    ImageView imgPostSmall;

    @FXML
    ImageView imgPostLarge;

    @FXML
    JFXToggleButton btnShowLarge;

    @FXML
    JFXButton btnAttach;

    @FXML
    StackPane stackPane;

    private UserEntity clientUserObject;

    private static EditPostController instance;

    private File imgFile;
    private static PostEntity postOld;
    private PostEntity newPost;

    private byte[] imgByte;

    public static EditPostController getInstance() {
        return instance;
    }

    //TODO Setup parsing system as well as formatting (User clicks bold for example, which add's '<b> </b>' and all text between that will be bold or something)
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        clientUserObject = LoginController.getUserEntity();
        titleText.setText(postOld.getTitle());
        postText.setText(postOld.getPostText());
        tagsText.setText(postOld.getTags());
        Image postPic = FileHelper.getPostPic(postOld);
        if(postPic != null){
            imgPostLarge.setImage( postPic );
            imgPostSmall.setImage( postPic );
        }

        imgByte =postOld.getImage();

    }

    public static void setPost(PostEntity post) {
        postOld = post;
    }

    public void setPostCreated(boolean success) {
        if (success) {
            Platform.runLater(() -> {
                missingEntryText.setTextFill(Paint.valueOf(Color.GREEN.toString()));
                missingEntryText.setText("Successfully updated!");
                missingEntryText.setVisible(true);
            });
        } else {
            Platform.runLater(() -> {
                missingEntryText.setTextFill(Paint.valueOf(Color.RED.toString()));
                missingEntryText.setText("Failed to update post");
                missingEntryText.setVisible(true);
            });
        }

    }

    public void handleBackButtonClick(MouseEvent mouseEvent) {
        PostEntity post = (newPost != null) ? newPost : postOld;
        ReadPostScreen.setPostDataObject(post);
        ClientMain.loadUI("ReadPostScreen", anchorPane);
    }

    public void handleSubmitButtonClick(ActionEvent actionEvent) {
        if (CreateRestTemplate.isConnected()) {
            String postTitle = this.titleText.getText();
            String postTags = this.tagsText.getText();
            String postText = this.postText.getText();
            if ((postTitle != null && !postTitle.isEmpty()) && (postTags != null && !postTags.isEmpty()) && (postText != null && !postText.isEmpty())) {
                if (! postOld.getTitle().equals(postTitle) && CreateRestTemplate.isExistPath("post/" + "isExist/" + clientUserObject.getUserName() + "/" + postTitle)) {
                    missingEntryText.setTextFill(Paint.valueOf(Color.RED.toString()));
                    missingEntryText.setText("The title of the post is duplicate!");
                    missingEntryText.setVisible(true);
                } else {
//                        UserEntity newUser = new UserEntity(usernameGiven, uuid, fullName, emailGiven, phoneNumber, passwordGiven,null,isMan,null,new Date());
                    PostEntity newPost = null;

                    newPost = new PostEntity(postOld.getOwnerId(), postTitle, postTags, postText, postOld.getDate(), imgByte);
                    System.out.println(newPost);
                    if (CreateRestTemplate.buildPost("post/edit/"+ postOld.getTitle()+"/"+LoginController.getToken(), PostEntity.class, newPost)) {
                        setPostCreated(true);
                        this.newPost = newPost;
                    } else {
                        setPostCreated(false);
                    }

                }
            } else {
                missingEntryText.setTextFill(Paint.valueOf(Color.RED.toString()));
                missingEntryText.setText("Missing entry!");
                missingEntryText.setVisible(true);
            }

        } else {
            missingEntryText.setTextFill(Paint.valueOf(new Color(1, 0.2, 0.2, 1).toString()));
            missingEntryText.setText("Error processing  request! Please try again later.");
            missingEntryText.setVisible(true);
        }
    }

    public void actionToggleButton(ActionEvent actionEvent) {
        if (orientationTxtArea.isSelected()) postText.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        else postText.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }

    public void attachAction(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        File file = fileChooser.showOpenDialog(null);

        try {
            if(file != null) {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                imgPostSmall.setImage(image);
                imgPostLarge.setImage(image);
                imgPostLarge.fitHeightProperty().bind(stackPane.heightProperty());
                imgPostLarge.fitWidthProperty().bind(stackPane.widthProperty());
                imgPostSmall.setVisible(true);

                imgFile=file;
                imgByte = FileHelper.imgAbsToBytes(imgFile);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void actionBtnShowLarge(ActionEvent actionEvent) {

        if(btnShowLarge.isSelected()){
            imgPostLarge.setVisible(true);
        }else{
            imgPostLarge.setVisible(false);
        }

    }
}