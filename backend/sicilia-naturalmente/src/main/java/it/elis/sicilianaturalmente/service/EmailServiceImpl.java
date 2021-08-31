package it.elis.sicilianaturalmente.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by abburi on 6/10/17.
 */

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired @Lazy
    private JavaMailSender javaMailSender;

    @Value("${mail.from.address}")
    private String fromAddress;

    /**
     * The method sends an email containing a template
     * @param toEmail
     * @param subject
     * @param message
     * @param file
     * @throws MessagingException
     */
    public void sendMailMultipart(String toEmail, String subject, String message, File file) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromAddress);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(message);

        if(file != null){
            helper.addAttachment(file.getName(), file);
        }
        javaMailSender.send(mimeMessage);
    }

    /**
     * The method sends an email to the address sent as a parameter
     * @param toEmail
     * @param subject
     * @param message
     * @throws MessagingException
     */
    public void sendMail(String toEmail, String subject, String message) throws MessagingException {
       sendMailMultipart(toEmail, subject, message, null);
    }

    /**
     * The method sends an email containing a file to the address sent as a parameter
     * @param toEmail
     * @param subject
     * @param message
     * @param file
     * @throws MessagingException
     */
    public void sendMail(String toEmail, String subject, String message, File file) throws MessagingException {
        sendMailMultipart(toEmail, subject, message, file);
    }
}
