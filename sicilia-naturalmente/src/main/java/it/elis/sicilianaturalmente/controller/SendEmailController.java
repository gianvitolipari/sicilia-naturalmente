package it.elis.sicilianaturalmente.controller;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Email;
import it.elis.sicilianaturalmente.model.Prodotto;
import it.elis.sicilianaturalmente.service.AccountService;
import it.elis.sicilianaturalmente.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by abburi on 6/18/17.
 */

@RestController
public class SendEmailController {

    @Autowired
    AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Value("${email.from.address}")
    private String toEmail;

    @RequestMapping(value = "/testSendEmail" , method = RequestMethod.POST)
    public ResponseEntity<String> sendEmail(@RequestBody Email email) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            Account account = accountService.whoami(request);
            emailService.sendMail(toEmail, "Nuova richiesta dall'utente: " + account.getEmail() , email.getMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Email correctly sent");
    }
}
