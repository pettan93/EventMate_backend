package gr.tei.erasmus.pp.eventmate.backend.models;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private ArrayList<File> content;

    @ManyToOne
    private User submitter;

    public Submission() {
    }

    public Submission(ArrayList<File> content, User submitter) {
        this.content = content;
        this.submitter = submitter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<File> getContent() {
        return content;
    }

    public void setContent(ArrayList<File> content) {
        this.content = content;
    }

    public User getSubmitter() {
        return submitter;
    }

    public void setSubmitter(User submitter) {
        this.submitter = submitter;
    }
}
