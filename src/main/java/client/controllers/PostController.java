package client.controllers;

import client.ClientMain;
import client.model.entity.PostEntity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class PostController implements Initializable {
    @FXML
    private Label title_text;

    @FXML
    private Label owner_text;

    @FXML
    private Label date_text;

    @FXML
    private Label tags_text;

    @FXML
    private Tooltip tagsToolTip;

    private PostEntity postDataObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setData(String titleText, String ownerText, String dateText, String tagTest) {
        title_text.setText(titleText);
        owner_text.setText(ownerText);
        date_text.setText(dateText);
        tags_text.setText(tagTest);
    }

    public void setData(PostEntity postDataObject) {
        this.postDataObject = postDataObject;
        title_text.setText(postDataObject.getTitle());
        owner_text.setText("By: " + postDataObject.getOwnerId());
        date_text.setText("Created on: " + ClientMain.formatDate( postDataObject.getDate() ) );
        tags_text.setText("Tag(s): " + postDataObject.getTags());
        tagsToolTip.setText("Tag(s): " + postDataObject.getTags());
    }

    public void onPostClicked(MouseEvent mouseEvent) {
        HomeScreenController.getInstance().loadPost(postDataObject);
        ReadPostScreen.setPostDataObject(postDataObject);
        ClientMain.loadUiToScene("ReadPostScreen");
    }
}
