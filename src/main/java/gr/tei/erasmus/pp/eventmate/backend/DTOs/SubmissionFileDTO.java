package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import gr.tei.erasmus.pp.eventmate.backend.enums.FileType;

import java.util.Date;

public class SubmissionFileDTO {

    private Long id;

    private FileType type;

    private Date created;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
