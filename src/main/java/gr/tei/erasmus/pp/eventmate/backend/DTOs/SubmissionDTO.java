package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import java.util.ArrayList;

public class SubmissionDTO {

    private Long id;

    private ArrayList<SubmissionFileDTO> content;

    private UserDTO submitter;

    private Integer maxPoints;

    private Integer earnedPoints;


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
}
