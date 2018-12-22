package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import gr.tei.erasmus.pp.eventmate.backend.enums.ReportType;

import java.util.Date;

public class ReportDTO {

    private Long id;

    private String name;

    private String comment;

    private ReportType type;

    private Date created;


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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
