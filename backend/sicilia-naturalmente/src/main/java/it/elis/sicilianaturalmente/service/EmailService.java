package it.elis.sicilianaturalmente.service;

import javax.mail.MessagingException;
import java.io.File;

public interface EmailService {
    public void sendMailMultipart(String toEmail, String subject, String message, File file) throws MessagingException;
    public void sendMail(String toEmail, String subject, String message) throws MessagingException;
    public void sendMail(String toEmail, String subject, String message, File file) throws MessagingException;
    }
