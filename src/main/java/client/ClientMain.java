package client;


import client.controllers.LoginController;
import client.model.CreateRestTemplate;
import client.model.FileHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientMain extends Application {
    public static Stage parentWindow;

    public static Scene scene;

    public static Stage stage;

    public static final String ICON_IMAGE_LOC = "/icon.png";

//    private static ClientNetworkMain networkManager;

    public static void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(ICON_IMAGE_LOC));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
            parentWindow = primaryStage;
            Parent root=null;
            try {
                LoginController.setUserEntity(FileHelper.getObject());
                LoginController.setToken(FileHelper.getToken());
                if(LoginController.getToken() == null || LoginController.getUserEntity() == null){
                    root = FXMLLoader.load(getClass().getResource("/fxml/LoginScreen.fxml"));
                }else {
                    root = FXMLLoader.load(getClass().getResource("/fxml/HomeScreen.fxml"));
                }
                if(LoginController.getToken() == null || LoginController.getUserEntity() == null){
                    root = FXMLLoader.load(getClass().getResource("/fxml/LoginScreen.fxml"));
                }
            }catch (Exception ex){
                root = FXMLLoader.load(getClass().getResource("/fxml/LoginScreen.fxml"));
            }

//            Parent root = FXMLLoader.load(getClass().getResource("view/sample.fxml"));

            scene = new Scene(root, 1187, 664);
            stage =primaryStage;
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setTitle("HK Studios");
            primaryStage.setResizable(false);

            setStageIcon(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getParentWindow() {
        return parentWindow;
    }

    public static File getUserObjTempRead(){
        try {
            return new File(System.getProperty("user.home")+"/user.ser");
        } catch (NullPointerException e) {
            return null;
        }
    }
    public static File getUserObjTempWrite(){
        return new File(System.getProperty("user.home")+"/user.ser");
    }

    public static File getUserTokenTemp(){
        return new File(System.getProperty("user.home")+"/token.txt");
    }

    public static Object loadUI(String ui, AnchorPane anchorPane) {
        Parent root = null;
        Object controller = null;
        try {

//            root = FXMLLoader.load(ClientMain.class.getResource("../resources/fxml/" + ui + ".fxml"));
            FXMLLoader loader = new FXMLLoader(ClientMain.class.getResource("/fxml/" + ui + ".fxml"));
            root = loader.load();
            controller = loader.getController();


        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            anchorPane.getChildren().setAll(root);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return controller;
    }

    public static Parent loadUI(String ui) {
        Parent root = null;

        try {
            root = FXMLLoader.load(ClientMain.class.getResource("/fxml/" + ui + ".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;

    }

    public static Object loadUiToScene (String ui ) {

        Parent root = null;
        Object controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(ClientMain.class.getResource("/fxml/" + ui + ".fxml"));
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setHeight(703);
        stage.setWidth(1203);
        scene.setRoot(root);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        return controller;
    }

    public static void loadUiToSceneResize ( String ui , double w ,double h) {
        Parent root = loadUI(ui);
        stage.setHeight(h);
        stage.setWidth(w);
        scene.setRoot(root);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public static void signOut (AnchorPane anchorPane){
        FileHelper.deleteObject();
        FileHelper.deleteToken();
        CreateRestTemplate.buildDeleteByPath("token/delete/"+
                LoginController.getToken()+"/"+
                LoginController.getUserEntity().getUserName());
        LoginController.setUserEntity(null);
        LoginController.setToken(null);
        ClientMain.loadUI("LoginScreen", anchorPane);
    }

    public static String formatDate(Date date){
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat formatter1 = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return formatter1.format(formatter.parse(date.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
