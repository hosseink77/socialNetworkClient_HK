package client.controllers;

import client.ClientMain;
import client.model.ConvertImage;
import client.model.CreateRestTemplate;
import client.model.entity.UserEntity;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileController implements Initializable {


    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXTextField full_name;

    @FXML
    private JFXTextField phone_number;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXPasswordField verified_password;

    @FXML
    private Label passwordsDoNotMatch;

    @FXML
    private Label emailNotValid;

    @FXML
    private Label missingEntry;

    @FXML
    private Rectangle passwordDetection;

    @FXML
    private Rectangle emailDetection;

    @FXML
    private JFXButton registerButton;

    @FXML
    private JFXComboBox<String> cbo;

    @FXML
    private JFXTextField status;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    private static EditProfileController instance;
    private UserEntity user;

    public static EditProfileController getInstance() {
        return instance;
    }

    public void setRegistered(boolean isRegistered) {

        if (isRegistered) {
            Platform.runLater(() -> {
                missingEntry.setTextFill(Paint.valueOf(Color.GREEN.toString()));
                missingEntry.setText("Updated Successfully!");
                missingEntry.setVisible(true);
            });
        } else {
            Platform.runLater(() -> {
                missingEntry.setTextFill(Paint.valueOf(Color.RED.toString()));
                missingEntry.setText("User already exists!");
                missingEntry.setVisible(true);
            });
        }

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        user = LoginController.getUserEntity();

        instance = this;
        full_name.setStyle("-fx-text-inner-color: #a0a2ab;");
        phone_number.setStyle("-fx-text-inner-color: #a0a2ab;");
        email.setStyle("-fx-text-inner-color: #a0a2ab;");
        username.setStyle("-fx-text-inner-color: #a0a2ab;");
        password.setStyle("-fx-text-inner-color: #a0a2ab;");
        status.setStyle("-fx-text-inner-color: #a0a2ab;");
        verified_password.setStyle("-fx-text-inner-color: #a0a2ab;");
        cbo.getItems().addAll("Man","Woman");
        cbo.setStyle("-fx-text-inner-color: #a0a2ab;-fx-border-color: #07b3ad;");
        cbo.setEditable(true);
        cbo.getEditor().setEditable(false);


        phone_number.setText(user.getPhoneNumber());
        username.setText(user.getUserName());
        full_name.setText(user.getFullName());
        email.setText( user.getEmail() );
        password.setText(user.getPassword());
        verified_password.setText(user.getPassword());
        if(user.getStatus() != null) {
            status.setText(user.getStatus());
        }

        cbo.setValue(  (user.isMan()) ? "Man":"Woman"  );


        phone_number.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (newValue.length()>11){
                    phone_number.setText(oldValue);
                }
                else if (!newValue.matches("\\d*") ) {
                    phone_number.setText(newValue.replaceAll("[^\\d]", ""));

                }
            }
        });

    }


    @FXML
    public void UpdateAction(ActionEvent e) {
        if (CreateRestTemplate.isConnected()) {

            String phoneNumber = phone_number.getText();
            String usernameGiven = username.getText();
            String fullName = full_name.getText();
            String emailGiven = email.getText();
            String passwordGiven = password.getText();
            String cboGender=cbo.getValue();
            String statusGiven = status.getText();

            boolean isMissingEntry = cboGender==null | fullName.isEmpty() || phoneNumber.isEmpty() || usernameGiven.isEmpty() || emailGiven.isEmpty() || passwordGiven.isEmpty() || verified_password.getText().isEmpty();

            if (passwordGiven.equals(verified_password.getText())) {
                passwordsDoNotMatch.setVisible(false);
                passwordDetection.setVisible(false);
            } else {
                System.out.println("passwords do not match! Main pass: " + passwordGiven + " other pass: " + verified_password.getText());
                passwordsDoNotMatch.setVisible(true);
                passwordDetection.setVisible(true);
            }

            if (isMissingEntry) {
                System.out.println("==================================");
                System.out.println("full_name = " + fullName + " | isEmpty: " + fullName.isEmpty());
                System.out.println("PHONE_number = " + phoneNumber + " | isEmpty: " + phoneNumber.isEmpty());
                System.out.println("email = " + emailGiven + " | isEmpty: " + emailGiven.isEmpty());
                System.out.println("gender = " + cboGender + " | isEmpty: " + cboGender==null +":gender");
                System.out.println("password = " + passwordGiven + " | isEmpty: " + passwordGiven.isEmpty());
                System.out.println("verified_password = " + verified_password.getText() + " | isEmpty: " + verified_password.getText().isEmpty());
                System.out.println("Missing an entry!");

                missingEntry.setTextFill(Paint.valueOf(new Color(1, 0.2, 0.2, 1).toString()));
                missingEntry.setText("You are missing an entry!");
                missingEntry.setVisible(true);
                return;
            } else {
                missingEntry.setVisible(false);
            }

//            if ( !emailNotValid.isVisible() && !passwordsDoNotMatch.isVisible() && !fullName.isEmpty() && !phoneNumber.isEmpty() && !usernameGiven.isEmpty() && !emailGiven.isEmpty() && !passwordGiven.isEmpty() && !verified_password.getText().isEmpty() && (passwordGiven.equals(verified_password.getText()))) {
            if ( !emailNotValid.isVisible() && !passwordsDoNotMatch.isVisible() && ! isMissingEntry) {

                boolean isMan= ( cboGender.length()>3 ) ? false: true;
                if (!user.getUserName().equals(usernameGiven) && CreateRestTemplate.isExistUserName(usernameGiven)){
                    setRegistered(false);
                }

                else{
                    if(statusGiven.isEmpty()) statusGiven = null;
                    user.set(usernameGiven,  fullName, emailGiven, phoneNumber, passwordGiven,isMan,statusGiven);
                    System.out.println(user);
                    if ( CreateRestTemplate.buildPost("edit",UserEntity.class,user) ){
                        setRegistered(true);
                        LoginController.setUserEntity(user);
                        try {
                            ConvertImage.saveObject(user);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }else{
                        missingEntry.setTextFill(Paint.valueOf(Color.RED.toString()));
                        missingEntry.setText("Failed to Updated Successfully!");
                        missingEntry.setVisible(true);
                    }

                }

            } else {
                System.out.println("DATA IS EMPTY");

                missingEntry.setTextFill(Paint.valueOf(new Color(1, 0.2, 0.2, 1).toString()));
                missingEntry.setText("You are missing an entry!");
                missingEntry.setVisible(true);
            }
        } else {
            missingEntry.setTextFill(Paint.valueOf(new Color(1, 0.2, 0.2, 1).toString()));
            missingEntry.setText("Error processing request! Please try again later.");
            missingEntry.setTranslateX(-60);
            missingEntry.setVisible(true);
        }
    }

    @FXML
    public void passwordKeyTypedAction(KeyEvent keyEvent) {
//        if (keyEvent.getCode().toString().equals(password.getText())) {
        if (verified_password.getText().equals(password.getText())) {
            passwordDetection.setVisible(false);
            passwordsDoNotMatch.setVisible(false);
        }
        else {
            passwordDetection.setVisible(true);
            passwordsDoNotMatch.setVisible(true);
        }
    }

    public void backButtonClickAction(MouseEvent mouseEvent) {
        ProfileScreenController profileScreen = (ProfileScreenController) ClientMain.loadUI("ProfileScreen", anchorPane);
        profileScreen.setupUser(user,user);
    }

    public void emailKeyTypedAction(KeyEvent keyEvent) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email.getText());
        if (matcher.find()) {
            emailDetection.setVisible(false);
            emailNotValid.setVisible(false);
        }
        else {
            emailDetection.setVisible(true);
            emailNotValid.setVisible(true);
        }

    }


}
