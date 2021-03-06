package gr.tei.erasmus.pp.eventmate.backend.models;

import gr.tei.erasmus.pp.eventmate.backend.enums.ReportType;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    private String comment;

    private ReportType type;

    private Blob content;

    private Date created;

    private Blob preview;

    @OneToOne
    private User reportCreator;

    public Report() {
    }

    public Report(String name, String comment, ReportType type, Blob content, Date created, Blob preview) {
        this.name = name;
        this.comment = comment;
        this.type = type;
        this.content = content;
        this.created = created;
        this.preview = preview;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Blob getPreview() {
        return preview;
    }

    public void setPreview(Blob preview) {
        this.preview = preview;
    }

    public User getReportCreator() {
        return reportCreator;
    }

    public void setReportCreator(User reportCreator) {
        this.reportCreator = reportCreator;
    }
}
