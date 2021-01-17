package client.controllers;

import client.ClientMain;
import client.model.FileHelper;
import client.model.CreateRestTemplate;
import client.model.entity.UserEntity;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXButton signInButton;

    @FXML
    private JFXButton signup;

    @FXML
    public JFXTextField username;

    @FXML
    private JFXCheckBox remember;

    @FXML
    private Label noUserFound;

    @FXML
    private JFXButton forgotpassword;

    @FXML
    private JFXPasswordField password;

    @FXML
    private Rectangle notification;

    @FXML
    private Label notificationTitle;

    @FXML
    private Label notificationText;

    @FXML
    private ImageView notificationSymbol;

    private boolean canLogin = false;

    private static LoginController instance;

    private static String usernameGiven1;

    private static UserEntity userEntity;

    private static String token;

    public static void setToken(String token) {
        LoginController.token = token;
    }

    public static String getToken() {
        return token;
    }

    public static UserEntity getUserEntity() {
        return userEntity;
    }

    public static void setUserEntity(UserEntity userEntity) {
        LoginController.userEntity = userEntity;
    }

    public LoginController() {
        instance = this;
    }

    public static LoginController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        username.setStyle("-fx-text-inner-color: #a0a2ab;");
        password.setStyle("-fx-text-inner-color: #a0a2ab;");
    }

    @FXML
    public void signInAction(ActionEvent e) {
        if (CreateRestTemplate.isConnected()) {
            String usernameGiven = username.getText();
            String passwordGiven = password.getText();

            usernameGiven1 = usernameGiven;

            if (!usernameGiven.isEmpty() && !passwordGiven.isEmpty()) {

                try {

                    String tok = CreateRestTemplate.buildGetToken(usernameGiven, passwordGiven);
                    System.out.println("token:" + tok);
                    if (tok != null) {
//                        loadUI("client/resources/fxml/HomeScreen.fxml");
                        UserEntity user = CreateRestTemplate.buildLogIn(tok);
                        System.out.println(user);
                        ;
                        userEntity = user;
                        token = tok;
                        try {
                            if (remember.isSelected()) {
                                FileHelper.saveObject(user);
                                FileHelper.saveToken(token);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        ClientMain.loadUI("HomeScreen", anchorPane);
                    } else {
                        if (tok == null) {
                            noUserFound.setVisible(true);
                            noUserFound.setText("User Not Found!");
                        }
//                        else {
//                            noUserFound.setVisible(true);
//                            noUserFound.setText("Password is wrong!");
//                        }
                    }

                } catch (NullPointerException ex) {

                    noUserFound.setVisible(true);
                    noUserFound.setText("Invalid!");

                }
            } else {
                noUserFound.setVisible(true);
                noUserFound.setText("Missing Entry!");
            }

        } else {
            showNotification();
        }
    }

    @FXML
    public void signUpAction(ActionEvent e1) {
        if (!CreateRestTemplate.isConnected()) {
            showNotification();
        } else {
            ClientMain.loadUI("SignupScreen", anchorPane);
        }
    }

    @FXML
    public void forgotPassAction(ActionEvent e1) throws IOException {

    }

    public String getUsername() {
        return username.getText();
    }

    public void showNotification(/*String title, String message*/) {
        notification.setVisible(true);
        notificationTitle.setVisible(true);
        notificationText.setVisible(true);
        notificationSymbol.setVisible(true);

        TranslateTransition notificationBox = new TranslateTransition(new Duration(350), notification);
        notificationBox.setToY(-(notification.getHeight()));
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), notification);

        if (notification.getTranslateY() != 0) {
            notificationBox.play();
        } else {
            closeNav.setToY(0);
            closeNav.play();
        }

        TranslateTransition notificationTitleAnimation = new TranslateTransition(new Duration(350), notificationTitle);
        notificationTitleAnimation.setToY(-(notificationTitle.getHeight()));
        TranslateTransition closeNotificationTitle = new TranslateTransition(new Duration(350), notificationTitle);

        if (notificationTitle.getTranslateY() != 0) {
            notificationTitleAnimation.play();
        } else {
            closeNotificationTitle.setToY(0);
            closeNotificationTitle.play();
        }

        TranslateTransition notificationTextAnimation = new TranslateTransition(new Duration(350), notificationText);
        notificationTextAnimation.setToY(-(notification.getHeight()));
        TranslateTransition closeNotificationText = new TranslateTransition(new Duration(350), notificationText);

        if (notificationText.getTranslateY() != 0) {
            notificationTextAnimation.play();
        } else {
            closeNotificationText.setToY(0);
            closeNotificationText.play();
        }

        TranslateTransition notificationSymbolAnimation = new TranslateTransition(new Duration(350), notificationSymbol);
        notificationTextAnimation.setToY(-(notification.getHeight()));
        TranslateTransition closeNotificationSymbol = new TranslateTransition(new Duration(350), notificationSymbol);

        if (notificationSymbol.getTranslateY() != 0) {
            notificationSymbolAnimation.play();
        } else {
            closeNotificationSymbol.setToY(0);
            closeNotificationSymbol.play();
        }
    }
}
