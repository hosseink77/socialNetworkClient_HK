package client.model;

import client.model.entity.PostEntity;
import client.model.entity.UserEntity;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ConvertImage {

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


    public static void saveObject(UserEntity obj) throws IOException {
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("/temp/user.ser");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(oos  != null) {
                oos.close();
            }
        }
    }

    public static UserEntity getObject() throws IOException {
        if (!(new File("/temp/user.ser").exists())) return null;

        ObjectInputStream objectinputstream = null;
        try {
            FileInputStream streamIn = new FileInputStream("/temp/user.ser");
            objectinputstream = new ObjectInputStream(streamIn);
            UserEntity user = (UserEntity) objectinputstream.readObject();
            objectinputstream.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectinputstream != null) {
                objectinputstream.close();
            }
        }
        return null;
    }

    public static boolean deleteObject(){
        File file = new File("/temp/user.ser");
        return file.delete();
    }

}
