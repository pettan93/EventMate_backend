package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import java.sql.Blob;

public class UserPublicDTO {

    private Long id;

    private String userName;

    private Blob photo;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }
}
