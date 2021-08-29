package it.elis.sicilianaturalmente.controller;

import io.swagger.annotations.*;
import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.RegexData;
import it.elis.sicilianaturalmente.model.Ruolo;
import it.elis.sicilianaturalmente.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    AccountService accountService;

    //@CrossOrigin(origins = {"http://localhost:3000"})

    //API for signup. An Account object is passed to register, the "email", "name" and "password" fields are required
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Account account) {
        RegexData regexData = account.validateAccount();
        if(regexData.isValid()){
            account.setRuolo(Ruolo.ROLE_CLIENT);
            return ResponseEntity.ok(accountService.signup(account));
        }else{
            return ResponseEntity.badRequest().body(regexData.getError());
        }

    }

    //API used to log in. The email and password fields are passed to check if the user is actually registered in the system
    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestParam("email") String email,@RequestParam("password") String password) {
        return ResponseEntity.ok(accountService.signin(email, password));
    }

    //API used to recover the password when the user forgets it. The email associated with the user for which the password has been forgotten will be sent
    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/forgotten")
    public ResponseEntity<String> passwordRecovery(@RequestParam("email") String email) throws MessagingException {
        accountService.passwordRecovery(email);
        return ResponseEntity.ok("A password recovery e-mail has been sent");
    }

}
