package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import gr.tei.erasmus.pp.eventmate.backend.models.Submission;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Blob;
import java.util.List;

public class TaskDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String place;

    private String description;

    private Long points;

    private List<Submission> submissions;

    private Blob photo;

    private Integer submissionsCount;

    private UserDTO taskOwner;

    private List<UserDTO> assignees;

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

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public Integer getSubmissionsCount() {
        return submissionsCount;
    }

    public void setSubmissionsCount(Integer submissionsCount) {
        this.submissionsCount = submissionsCount;
    }

    public UserDTO getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(UserDTO taskOwner) {
        this.taskOwner = taskOwner;
    }

    public List<UserDTO> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<UserDTO> assignees) {
        this.assignees = assignees;
    }
}
