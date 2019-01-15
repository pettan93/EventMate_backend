package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.EmailDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Report;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private EventService eventService;


    public void sendMessageWithAttachment(Report report, EmailDTO emailDTO) {

        MimeMessage message = emailSender.createMimeMessage();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(user.getEmail());
            helper.setTo(emailDTO.getRecipients().toArray(String[]::new));
            helper.setSubject(emailDTO.getSubject());
            helper.setText(emailDTO.getText());

            // todo change to pdf
            File tempFile = File.createTempFile(report.getName() + ".jpg", ".jpg");

            helper.addAttachment(report.getName(), FileUtils.getFileFromBlob(report.getContent(), tempFile));

            tempFile.deleteOnExit();

            emailSender.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }


    public void sendEmailInvitation(String  targetEmail, Event event) {

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom("invite@eventmate.com");
            helper.setTo(targetEmail);
            helper.setSubject("EventMate Invitation");
            helper.setText("Try EventMate, mate! You have been invited for event " + event.getName());


            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }
}
