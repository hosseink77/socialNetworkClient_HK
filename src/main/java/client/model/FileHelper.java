package client.model;

import client.ClientMain;
import client.model.entity.PostEntity;
import client.model.entity.UserEntity;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

public class FileHelper {

    public static byte[] imgToBytes(String imageName) throws IOException {
        BufferedImage bImage = ImageIO.read(new File("/temp/" + imageName + ".jpg"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        return bos.toByteArray();
    }

    public static void bytesToImg(byte[] data, String imageName) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage bImage2 = ImageIO.read(bis);
        ImageIO.write(bImage2, "jpg", new File("/temp/" + imageName + ".jpg"));
        System.out.println("image created");
    }

    public static byte[] imgAbsToBytes(File file) throws IOException {
        if(file.exists()) {
            BufferedImage bImage = ImageIO.read(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos);
            return bos.toByteArray();
        }
        return null;
    }


    public static boolean existsImg(String imageName) {
        return new File("/temp/" + imageName + ".jpg").exists();
    }

    public static Image getProfile(UserEntity user) {
            if (user.getImage() != null) {

                ByteArrayInputStream bis = new ByteArrayInputStream(user.getImage());
                BufferedImage bImage2 = null;
                try {
                    bImage2 = ImageIO.read(bis);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Image image = SwingFXUtils.toFXImage(bImage2, null);
                return image;
            } else {
                String defPic =(user.isMan()) ? "/temp/m_avatar.png" : "/temp/w_avatar.png";
                return new Image(defPic);

            }
    }

    public static Image getPostPic(PostEntity post) {
        if (post.getImage() != null) {

            ByteArrayInputStream bis = new ByteArrayInputStream(post.getImage());
            BufferedImage bImage2 = null;
            try {
                bImage2 = ImageIO.read(bis);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image image = SwingFXUtils.toFXImage(bImage2, null);
            return image;
        }
        return null;
    }

    public static void saveToken (String token){
        try {
            try( PrintWriter pw = new PrintWriter(ClientMain.getUserTokenTemp()) ) {
                pw.print(token);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static String getToken (){
        File file = ClientMain.getUserTokenTemp();
        if(file.exists()){
            try {
                try( Scanner sc = new Scanner(file) ){
                    String token = sc.next();
                    if(token != null && ! token.isEmpty()){
                        return token;
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void deleteToken(){
        File file = null;
        try {
            file = ClientMain.getUserTokenTemp();
            try( PrintWriter pw = new PrintWriter(file) ) {
                pw.print("");
            }
        } catch (NullPointerException | FileNotFoundException nEx){
            System.out.println("ObjectFile Not Found");
        }
    }


    public static void saveObject(UserEntity obj) throws IOException {
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;

        File fileUserObj = ClientMain.getUserObjTempWrite();

        if (fileUserObj == null) {
            System.out.println("null");
            return;
        }

        if (!(fileUserObj.exists())){
            System.out.println("not exists");
            return ;
        }

        try {
            fout = new FileOutputStream(fileUserObj);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(obj);
            System.out.println(fileUserObj.getAbsolutePath());
            System.out.println("writeObject");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(oos  != null) {
                oos.close();
            }
        }
    }

    public static UserEntity getObject() throws IOException {

        File fileUserObj = ClientMain.getUserObjTempRead();
        if (fileUserObj == null)
            return null;

        if (!(fileUserObj.exists())) return null;

        ObjectInputStream objectinputstream = null;
        try {
            FileInputStream streamIn = new FileInputStream(fileUserObj);
            objectinputstream = new ObjectInputStream(streamIn);
            UserEntity user = (UserEntity) objectinputstream.readObject();
            objectinputstream.close();
            return user;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        } finally {
            if (objectinputstream != null) {
                objectinputstream.close();
            }
        }
    }

    public static void deleteObject(){
        File file = null;
        try {
             file = ClientMain.getUserObjTempWrite();
             try( PrintWriter pw = new PrintWriter(file) ) {
                 pw.print("");
             }
        } catch (NullPointerException | FileNotFoundException nEx){
            System.out.println("ObjectFile Not Found");
        }
    }

}
