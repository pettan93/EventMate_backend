package gr.tei.erasmus.pp.eventmate.backend.DTOs;

public class UserDTO {

    private Long id;

    private String userName;

    private String email;

    private String photo;

    private String password;

    private Integer score;

    private Integer organizedEvents;

    private Integer attendedEvents;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getOrganizedEvents() {
        return organizedEvents;
    }

    public void setOrganizedEvents(Integer organizedEvents) {
        this.organizedEvents = organizedEvents;
    }

    public Integer getAttendedEvents() {
        return attendedEvents;
    }

    public void setAttendedEvents(Integer attendedEvents) {
        this.attendedEvents = attendedEvents;
    }
}
