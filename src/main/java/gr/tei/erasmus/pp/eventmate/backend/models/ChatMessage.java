package gr.tei.erasmus.pp.eventmate.backend.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User from;

    @OneToOne
    private User to;

    private Date date;

    private String content;

    public ChatMessage() {
    }

    public ChatMessage(User from, User to, Date date, String content) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.content = content;
    }

    public ChatMessage(User from, User to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
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

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", date=" + date +
                ", content='" + content + '\'' +
                '}';
    }
}
