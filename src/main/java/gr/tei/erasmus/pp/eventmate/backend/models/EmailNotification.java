package gr.tei.erasmus.pp.eventmate.backend.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmailNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Invitation invitation;

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



}
