package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import java.util.ArrayList;

public class SubmissionDTO {

    private Long id;

    private String taskName;

    private String taskPhoto;

    private String taskDescription;

    private ArrayList<SubmissionFileDTO> content;

    private UserDTO submitter;

    private Integer maxPoints;

    private Integer earnedPoints;

    private Long parentTaskId;


    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<SubmissionFileDTO> getContent() {
        return content;
    }

    public void setContent(ArrayList<SubmissionFileDTO> content) {
        this.content = content;
    }

    public UserDTO getSubmitter() {
        return submitter;
    }

    public void setSubmitter(UserDTO submitter) {
        this.submitter = submitter;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Integer getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(Integer earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskPhoto() {
        return taskPhoto;
    }

    public void setTaskPhoto(String taskPhoto) {
        this.taskPhoto = taskPhoto;
    }

    @Override
    public String toString() {
        return "SubmissionDTO{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", taskPhoto='" + taskPhoto + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", content=" + content +
                ", submitter=" + submitter +
                ", maxPoints=" + maxPoints +
                ", earnedPoints=" + earnedPoints +
                '}';
    }
}
