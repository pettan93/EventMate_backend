package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

public class ChatMessageDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UserLightDTO from;

    private UserLightDTO to;

    private Date date;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserLightDTO getFrom() {
        return from;
    }

    public void setFrom(UserLightDTO from) {
        this.from = from;
    }

    public UserLightDTO getTo() {
        return to;
    }

    public void setTo(UserLightDTO to) {
        this.to = to;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
