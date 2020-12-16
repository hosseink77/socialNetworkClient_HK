package client.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {
    private String userName;
    private String uuid;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    private byte[] image;
    private boolean isMan;
    private  String status;
    private Date dateJoin;

    public void set(String userName, String fullName, String email, String phoneNumber, String password, boolean isMan, String status) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isMan = isMan;
        this.status = status;
    }
}
