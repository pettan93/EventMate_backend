package gr.tei.erasmus.pp.eventmate.backend.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private  ArrayList<Permission> permissions;

    private String place;

    private String description;

    private Byte points;

    private  ArrayList<Submission> submissions;

    public Task() {
    }

    public Task(String name,
                ArrayList<Permission> permissions,
                String place,
                String description,
                Byte points,
                ArrayList<Submission> submissions) {
        this.name = name;
        this.permissions = permissions;
        this.place = place;
        this.description = description;
        this.points = points;
        this.submissions = submissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getPoints() {
        return points;
    }

    public void setPoints(Byte points) {
        this.points = points;
    }

    public ArrayList<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(ArrayList<Submission> submissions) {
        this.submissions = submissions;
    }
}
