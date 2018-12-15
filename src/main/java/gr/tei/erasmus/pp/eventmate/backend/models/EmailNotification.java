package gr.tei.erasmus.pp.eventmate.backend.models;

import javax.persistence.*;

@Entity
public class EmailNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Invitation invitation;

    @OneToOne
    private Event event;

    private String content;

    private String stamp;

    public EmailNotification() {
    }

    public EmailNotification(Invitation invitation, Event event, String content, String stamp) {
        this.invitation = invitation;
        this.event = event;
        this.content = content;
        this.stamp = stamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invitation getInvitation() {
        return invitation;
    }

    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }
}
