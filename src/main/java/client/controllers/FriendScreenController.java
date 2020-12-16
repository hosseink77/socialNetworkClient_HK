package client.controllers;

import client.ClientMain;
import client.model.CreateRestTemplate;
import client.model.entity.FriendEntity;
import client.model.entity.PostEntity;
import client.model.entity.UserEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FriendScreenController  implements Initializable {

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Label errorConnection;

    @FXML
    private Label errorNotFound;

    @FXML
    private TextField username;

    @FXML
    private ScrollPane friendsPane;

    @FXML
    private VBox friendListPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setStyle("-fx-text-inner-color: #a0a2ab;");
        if (CreateRestTemplate.isConnected()){
            loadFriendsHome();
        }else{
            errorConnection.setTextFill(Paint.valueOf(new Color(1, 0.2, 0.2, 1).toString()));
            errorConnection.setText("Connection failed to load friends!!! Please try again later.");
            errorConnection.setVisible(true);
        }
    }

    public void loadFriendsHome() {
        friendListPane.getChildren().clear();
        List<UserEntity> friendList = CreateRestTemplate.buildGetListFriend("friend/"+LoginController.getUserEntity().getUserName() );
        setupFriends(friendList);
    }

    private void setupFriends(List<UserEntity> friendList) {
        try {
            friendsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            Node[] nodes = new Node[friendList.size()];

            for (int i = 0; i < friendList.size(); i++) {
                try {

                    final int j = i;

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Friend.fxml"));

                    nodes[i] = loader.load();

                    UserEntity friendDataObject = friendList.get(i);

                    FriendController friendController = (FriendController) loader.getController();

                    if (friendController == null) {
                        System.out.println("CONTROLLER IS NULL");
                    }

                    friendController.setData(friendDataObject, this );

                    nodes[i].setOnMouseEntered(event -> {
                        nodes[j].setStyle("-fx-background-color : #0A0E3F");
                    });
                    nodes[i].setOnMouseExited(event -> {
                        nodes[j].setStyle("-fx-background-color : #02030A");
                    });
                    friendListPane.getChildren().add(nodes[i]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ReadPostScreen.setBackPage("HomeScreen");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }


    public void handleBackButtonClick(MouseEvent mouseEvent) {
        ClientMain.loadUiToScene("HomeScreen");
    }

    public void handleAddFriendAction(ActionEvent actionEvent) {
        String userId = LoginController.getUserEntity().getUserName();
        String friendId = username.getText();
        if (! friendId.isEmpty() ){
            if(CreateRestTemplate.isConnected()){
                if(! friendId.equals(userId) && CreateRestTemplate.isExistUserName(friendId)) {
                    FriendEntity obj = new FriendEntity(userId, friendId);
                    if( ! CreateRestTemplate.isExistPath("friend/isExist/"+userId+"/"+friendId) ) {
                        if (CreateRestTemplate.buildPost("friend/", FriendEntity.class, obj)) {
                            errorNotFound.setText("User successfully added");
                            errorNotFound.setTextFill(Paint.valueOf(Color.GREEN.toString()));
                            errorNotFound.setVisible(true);
                            loadFriendsHome();
                        }else {
                            visibleErrorNotFound("Error !");
                        }
                    }else{
                        visibleErrorNotFound("This is a duplicate user !");
                    }
                }else{
                    visibleErrorNotFound("User not found!");
                }
            }else{
                errorConnection.setVisible(true);
            }
        }
    }

    public void visibleErrorNotFound(String errorText){
        errorNotFound.setText(errorText);
        errorNotFound.setTextFill(Paint.valueOf(Color.RED.toString()));
        errorNotFound.setVisible(true);
    }

}
