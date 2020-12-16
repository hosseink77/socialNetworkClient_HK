package client.controllers;

import client.model.CreateRestTemplate;
import client.model.entity.UserEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.MalformedURLException;

public class Controller {

    @FXML
    Label myLabel;

    @FXML
    ImageView img;

    String baseUrl="http://localhost:8080/";

    @FXML
    public void btnClick(ActionEvent actionEvent) {

//        UserEntity user = CreateRestTemplate.buildGet("", UserEntity.class,"negar6");
//        try {
//            ResponseEntity<Boolean> bool = CreateRestTemplate.buildObject().getForEntity(baseUrl, boolean.class);
//            if(bool.getBody()){
//            System.out.println("true=> "+ bool.getBody());
//            }else System.out.println("false=> "+bool.getBody());
//        }catch (HttpClientErrorException.NotFound ex){
//            System.out.println("404 not found");
//        }
//        catch (ResourceAccessException ex){
//            System.out.println("ResourceAccessException : No Connection");
//        }


//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> entity = restTemplate.getForEntity(baseUrl, String.class);

//        myLabel.setText(entity.getBody());
//        System.out.println();
//        if (user != null)
//            myLabel.setText(user.toString());
//        myLabel.setText("heeeeeeeeeeeeeeeeeellllllllllllllllllllooooooooooooo");

//        try {
//            UserEntity user=new UserEntity("negarTest","321","negarNew","neg@gm.com","091425252","2587");
//            UserEntity user=new UserEntity();
//            user.setData("nTest","321","negarNew","neg@gm.com","091425252","2587");
//            System.out.println(user);
//            boolean b = CreateRestTemplate.buildPost("",UserEntity.class,user);
//        boolean b = CreateRestTemplate.isExistUserName("negarr");
//        UserEntity b = CreateRestTemplate.buildGet("", UserEntity.class, "hossein");
//        ResponseEntity<UserEntity> b = CreateRestTemplate.buildObject().postForEntity(baseUrl, user, UserEntity.class);
//        boolean b = CreateRestTemplate.buildPost("add", int.class, 777);

//        myLabel.setText("isExits:  "+b);

//        }catch (Exception ex){
//            System.out.println(ex.getClass());
//        }
//        Image photo=new Image("client/resources/icon.png");
        Image photo=new Image("./temp/w_avatar.png");
        File f=new File("./src/temp/w_avatar.png");
        try {
            System.out.println( f.toURI().toURL() );
        } catch (MalformedURLException e) {
            System.out.println("Errror");
        }
//        Image photo=new Image("https://c7.alamy.com/comp/KPDWWM/bamboo-tree-forest-beams-god-rays-straight-path-h-KPDWWM.jpg");
//        Image photo=new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTcLys7U1vEif039QsQtvL5meAyIU8HwjlMdA&usqp=CAU");
        img.setImage(photo);
//        myLabel.setText(String.valueOf(f.exists()));
        myLabel.setText(String.valueOf(f.exists()));
        System.out.println(f.getAbsolutePath());


    }
}
