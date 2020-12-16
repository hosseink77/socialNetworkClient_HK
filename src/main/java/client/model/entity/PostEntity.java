package client.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEntity implements Serializable {
    private String ownerId;
    private String title;
    private String tags;
    private String postText;
    private Date date;
    private byte[] image;

}
