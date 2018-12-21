package gr.tei.erasmus.pp.eventmate.backend.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<SubmissionFile> content;

    @ManyToOne
    private User submitter;


    private Integer earnedPoints;

    public Submission() {
    }

    public Integer getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(Integer earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SubmissionFile> getContent() {
        return content;
    }

    public void setContent(List<SubmissionFile> content) {
        this.content = content;
    }

    public User getSubmitter() {
        return submitter;
    }

    public void setSubmitter(User submitter) {
        this.submitter = submitter;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", content=" + content +
                ", submitter=" + submitter +
                ", earnedPoints=" + earnedPoints +
                '}';
    }
}
