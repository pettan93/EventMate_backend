package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.EmailDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.Report;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
}
