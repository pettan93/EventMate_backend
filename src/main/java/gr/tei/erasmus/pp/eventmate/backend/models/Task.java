package gr.tei.erasmus.pp.eventmate.backend.models;

import gr.tei.erasmus.pp.eventmate.backend.enums.TaskState;

import javax.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToOne
    private User taskOwner;

    @ManyToMany
    private List<User> assignees;

    private String place;

    private String description;

    private Long points;

    private TaskState taskState;

    private Boolean timeLimited;

    private Integer remainingTime;

    private Integer timeLimit;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Submission> submissions;

    private Blob photo;

    public Task() {
    }

    public Task(String name,
                String place,
                String description,
                Long points,
                List<Submission> submissions) {
        this.name = name;
        this.place = place;
        this.description = description;
        this.points = points;
        this.submissions = submissions;
    }

    public Task(String name,
                String place,
                String description,
                Long points,
                ArrayList<Submission> submissions,
                User taskOwner,
                TaskState taskState) {
        this.name = name;
        this.place = place;
        this.description = description;
        this.points = points;
        this.submissions = submissions;
        this.taskOwner = taskOwner;
        this.taskState = taskState;
    }

    public Task(
            Long id,
            String name,
            User taskOwner,
            List<User> assignees,
            String place,
            String description,
            Long points,
            Boolean timeLimited,
            Integer timeLimit) {
        this.id = id;
        this.name = name;
        this.taskOwner = taskOwner;
        this.assignees = assignees;
        this.place = place;
        this.description = description;
        this.points = points;
        this.timeLimited = timeLimited;
        this.timeLimit = timeLimit;
    }

    public Task(
            String name,
            User taskOwner,
            List<User> assignees,
            String place,
            String description,
            Long points,
            Boolean timeLimited,
            Integer timeLimit) {
        this.name = name;
        this.taskOwner = taskOwner;
        this.assignees = assignees;
        this.place = place;
        this.description = description;
        this.points = points;
        this.timeLimited = timeLimited;
        this.timeLimit = timeLimit;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public Boolean getTimeLimited() {
        return timeLimited;
    }

    public void setTimeLimited(Boolean timeLimited) {
        this.timeLimited = timeLimited;
    }

    public Integer getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Integer remainingTime) {
        this.remainingTime = remainingTime;
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

    public User getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(User taskOwner) {
        this.taskOwner = taskOwner;
    }

    public List<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<User> assignees) {
        this.assignees = assignees;
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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", taskOwner=" + taskOwner +
                ", assignees=" + assignees +
                ", place='" + place + '\'' +
                ", description='" + description + '\'' +
                ", points=" + points +
                ", taskState=" + taskState +
                ", timeLimited=" + timeLimited +
                ", remainingTime=" + remainingTime +
                ", timeLimit=" + timeLimit +
                ", submissions=" + submissions +
                '}';
    }
}

