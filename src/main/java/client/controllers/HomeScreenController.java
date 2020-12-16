package client.controllers;

import client.ClientMain;
import client.model.ConvertImage;
import client.model.CreateRestTemplate;
import client.model.entity.PostEntity;
import client.model.entity.UserEntity;
import com.jfoenix.controls.JFXButton;
//import client.handlers.ClientDataHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeScreenController implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label fullName;

    @FXML
    private Button profileButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button friendsButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button signOutButton;

    @FXML
    private ImageView profilePic;

    @FXML
    private ScrollPane postsPane;

    @FXML
    private VBox postListPane = null;

    @FXML
    private JFXButton createPostButton;

    @FXML
    private Label errorText;

    private UserEntity user;

    private static HomeScreenController instance;

    public HomeScreenController() {
        instance = this;
    }

    public static HomeScreenController getInstance() {
        return instance;
    }

    private String profileName;
    private String profilePath;
    private String defaultProfilePath;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            user = LoginController.getUserEntity();
            profileName = "profilePic_" + user.getUserName();
            profilePath = "./temp/profilePic_" + user.getUserName() + ".jpg";

            defaultProfilePath = (user.isMan()) ? "./temp/m_avatar.png" : "./temp/w_avatar.png";
            fullName.setText(user.getFullName());
            setClientUser();
        }catch (NullPointerException ex){
//            ex.printStackTrace();
            LoginController.setUserEntity(null);
            ConvertImage.deleteObject();
            ClientMain.loadUI("LoginScreen", anchorPane);
            return;
        }
        if (CreateRestTemplate.isConnected()){
            loadPostsinHome();
        }else{
            errorText.setTextFill(Paint.valueOf(new Color(1, 0.2, 0.2, 1).toString()));
            errorText.setText("Connection failed to load posts!!! Please try again later.");
            errorText.setVisible(true);
        }

    }

    public void setClientUser() {
        setProfilePic( ConvertImage.getProfile(user) );
    }

    public void handleMenuButtonClick(ActionEvent actionEvent) {


        if (actionEvent.getSource() == profileButton) {

            ProfileScreenController profileScreen = (ProfileScreenController) ClientMain.loadUI("ProfileScreen", anchorPane);
            profileScreen.setupUser(user,user);
        }
        if (actionEvent.getSource() == homeButton) {

            if (CreateRestTemplate.isConnected()){
                postListPane.getChildren().clear();
                loadPostsinHome();
            }else{
                errorText.setTextFill(Paint.valueOf(new Color(1, 0.2, 0.2, 1).toString()));
                errorText.setText("Connection failed to load posts!!! Please try again later.");
                errorText.setVisible(true);
            }

            System.out.println("Home button pressed!!!");
        }
        if (actionEvent.getSource() == friendsButton) {
            System.out.println("Friends button pressed!!!");
            ClientMain.loadUiToSceneResize("FriendScreen",735,668);

        }
        if (actionEvent.getSource() == settingsButton) {
            System.out.println("Settings button pressed!!!");
        }
//
//        //FIXME Either remove this, OR be sure you clear ALL client data related to the last account logged in
        if (actionEvent.getSource() == signOutButton) {
            LoginController.setUserEntity(null);
            ConvertImage.deleteObject();
            ClientMain.loadUI("LoginScreen", anchorPane);
        }
    }

    public void loadPostsinHome(){
        List<PostEntity> postList = CreateRestTemplate.buildGetListPost("post/getAll/"+user.getUserName() );
        setupPosts(postList);
    }

    public void setupPosts(List<PostEntity> postDataObjects) {
        try {
            postsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            Node[] nodes = new Node[postDataObjects.size()];

            for (int i = 0; i < postDataObjects.size(); i++) {
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
            ReadPostScreen.setBackPage("HomeScreen");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public void setProfilePic(Image img) {
        this.profilePic.setImage(img);

        Rectangle clip = new Rectangle(
                profilePic.getFitWidth(), profilePic.getFitHeight()
        );

        clip.setArcWidth(120);
        clip.setArcHeight(120);
        clip.setStroke(Color.SEAGREEN);
        clip.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        profilePic.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = profilePic.snapshot(parameters, null);

        profilePic.setClip(null);

        profilePic.setEffect(new DropShadow(20, Color.GREEN));

        profilePic.setImage(image);
    }

    public void loadPost(PostEntity postDataObject) {
        ReadPostScreen.setPostDataObject(postDataObject);
        ClientMain.loadUI("ReadPostScreen", anchorPane);
    }

    public void handleCreatePostButton(ActionEvent actionEvent) {
        ClientMain.loadUI("CreatePostScreen",anchorPane);
    }
}
