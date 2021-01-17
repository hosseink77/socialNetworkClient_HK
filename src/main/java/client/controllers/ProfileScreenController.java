package client.controllers;

import client.ClientMain;
import client.model.ConvertImage;
import client.model.CreateRestTemplate;
import client.model.entity.PostEntity;
import client.model.entity.UserEntity;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileScreenController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    private File imgFile;
    @FXML
    private ScrollPane postsPane;
    @FXML
    private VBox postListPane;

    public ImageView getProfilePic() {
        return profilePic;
    }

    @FXML
    private ImageView profilePic;

    @FXML
    private Label fullNameText;

    @FXML
    private Label usernameText;

    @FXML
    private Label phoneNumberText;

    @FXML
    private Label dateJoinedText;

    @FXML
    private Label statusText;

    @FXML
    ImageView imgProfileLarge;

    @FXML
    JFXToggleButton btnShowLarge;

    @FXML
    JFXButton btnAttach;

    @FXML
    StackPane stackPane;

    @FXML
    Label erorrText;

    @FXML
    JFXButton addEditButton;

    public UserEntity getClientUserObject() {
        return clientUserObject;
    }

    public UserEntity getUserObject() {
        return userObject;
    }

    /**
     * This is the user that Logined
     */
    private UserEntity clientUserObject;

    /**
     * This is the user that we're viewing
     */
    private UserEntity userObject;

    private static ProfileScreenController instance;

    public static ProfileScreenController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setupUser(UserEntity clientUserObject, UserEntity userToView) {
        this.clientUserObject = clientUserObject;
        this.userObject = userToView;
        loadPostsinProfile();
        if(userToView != clientUserObject ){
            btnAttach.setVisible(false);
            addEditButton.setVisible(false);
        }
//        if (userToView.getImage() != null) {
            this.profilePic.setImage( ConvertImage.getProfile(userToView) );
            this.imgProfileLarge.setImage( ConvertImage.getProfile(userToView) );
//        }

        this.fullNameText.setText(userToView.getFullName());
        this.usernameText.setText(userToView.getUserName());
        this.phoneNumberText.setText(userToView.getPhoneNumber());
        this.dateJoinedText.setText(ClientMain.formatDate( userToView.getDateJoin() ) );
        if(userToView.getStatus() != null) {
            this.statusText.setText(userToView.getStatus());
        }else {
            this.statusText.setText("There is not status");
        }

        instance =this;
    }

    public void handleAddEditAction(ActionEvent actionEvent) {
        ClientMain.loadUI("EditProfileScreen",anchorPane);
    }

    public void handleBackButtonClick(MouseEvent mouseEvent) {

        ClientMain.loadUI("HomeScreen",anchorPane);
    }

    public void actionBtnShowLarge(ActionEvent actionEvent) {

        if(btnShowLarge.isSelected()){
            imgProfileLarge.setVisible(true);
        }else{
            imgProfileLarge.setVisible(false);
        }
    }

    public void attachAction(ActionEvent actionEvent) {
        if(CreateRestTemplate.isConnected()) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
            File file = fileChooser.showOpenDialog(null);
            imgFile = file;
            try {
                if (file != null) {
                    userObject.setImage(ConvertImage.imgAbsToBytes(file));
                    if(CreateRestTemplate.buildPost("edit/"+LoginController.getToken(), UserEntity.class, userObject)) {
                        setProfileEdited(true);
                        LoginController.setUserEntity(userObject);
                        try {
                            ConvertImage.saveObject(userObject);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        BufferedImage bufferedImage = ImageIO.read(file);
                        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                        profilePic.setImage(image);
                        imgProfileLarge.setImage(image);
                        imgProfileLarge.fitHeightProperty().bind(stackPane.heightProperty());
                        imgProfileLarge.fitWidthProperty().bind(stackPane.widthProperty());
                        profilePic.setVisible(true);
                    } else {
                        setProfileEdited(false);
                    }

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else{
            erorrText.setTextFill(Paint.valueOf(new Color(1, 0.2, 0.2, 1).toString()));
            erorrText.setText("Error processing \n request !");
            erorrText.setVisible(true);
        }
    }

    public void setProfileEdited(boolean success) {
        if (success) {
            Platform.runLater(() -> {
                erorrText.setTextFill(Paint.valueOf(Color.GREEN.toString()));
                erorrText.setText("Successfully edit");
                erorrText.setVisible(true);
            });
        } else {
            Platform.runLater(() -> {
                erorrText.setTextFill(Paint.valueOf(Color.RED.toString()));
                erorrText.setText("Failed to edit !");
                erorrText.setVisible(true);
            });
        }

    }

    public void loadPostsinProfile(){
        List<PostEntity> postList = CreateRestTemplate.buildGetListPost("post/"+userObject.getUserName() );
        setupPosts(postList);
    }

    public void setupPosts(List<PostEntity> postDataObjects) {
        try {
            postsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            Node[] nodes = new Node[postDataObjects.size()];

            for (int i = postDataObjects.size()-1; i >= 0; i--) {
                try {

                    final int j = i;

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Post.fxml"));

                    nodes[i] = loader.load();

                    PostEntity postDataObject = postDataObjects.get(i);

                    PostController postController = (PostController) loader.getController();


                    if (postController == null) {
                        System.out.println("CONTROLLER IS NULL");
                    }

                    postController.setData(postDataObject);

                    nodes[i].setOnMouseEntered(event -> {
                        nodes[j].setStyle("-fx-background-color : #0A0E3F");
                    });
                    nodes[i].setOnMouseExited(event -> {
                        nodes[j].setStyle("-fx-background-color : #02030A");
                    });
                    postListPane.getChildren().add(nodes[i]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ReadPostScreen.setBackPage("ProfileScreen");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

}
