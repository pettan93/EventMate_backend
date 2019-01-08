package gr.tei.erasmus.pp.eventmate.backend.models;

import gr.tei.erasmus.pp.eventmate.backend.enums.FileType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Blob;
import java.util.Date;

@Entity
public class SubmissionFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Blob content;

    private String name;

    private String comment;

    private FileType type;

    private Date created;

    public SubmissionFile() {
    }

    public SubmissionFile(Blob content, FileType type, Date created) {
        this.content = content;
        this.type = type;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "SubmissionFile{" +
                "id=" + id +
                ", content=" + content +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", type=" + type +
                ", created=" + created +
                '}';
    }
}
