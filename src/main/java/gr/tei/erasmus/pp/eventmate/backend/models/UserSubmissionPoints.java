package gr.tei.erasmus.pp.eventmate.backend.models;

import javax.persistence.Entity;

@Entity
public class UserSubmissionPoints {

    private long idUser;

    private long points;

    public UserSubmissionPoints() {
    }

    public UserSubmissionPoints(long idUser, long points) {
        this.idUser = idUser;
        this.points = points;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }
}
