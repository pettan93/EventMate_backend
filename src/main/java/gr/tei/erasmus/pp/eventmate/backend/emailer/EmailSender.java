package gr.tei.erasmus.pp.eventmate.backend.emailer;

import gr.tei.erasmus.pp.eventmate.backend.repository.EmailNotificationRepository;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {


    private final EmailNotificationRepository emailNotificationRepository;


    public EmailSender(EmailNotificationRepository emailNotificationRepository) {
        this.emailNotificationRepository = emailNotificationRepository;
    }




}
