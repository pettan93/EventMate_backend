package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import gr.tei.erasmus.pp.eventmate.backend.enums.TaskState;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

public class TaskDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String place;

    private String description;

    private Long points;

    private List<SubmissionDTO> submissions;

    private String photo;

    private TaskState taskState;

    private Boolean timeLimited;

    private Integer remainingTime;

    private Integer timeLimit;

    /************/

    private Integer submissionsCount;

    private Long eventId;

    private UserDTO taskOwner;

    private List<UserDTO> assignees;

    public TaskState getTaskState() {
        return taskState;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    /************/




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

    public List<SubmissionDTO> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<SubmissionDTO> submissions) {
        this.submissions = submissions;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
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
