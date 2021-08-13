package it.elis.sicilianaturalmente.controller;

import io.swagger.annotations.*;
import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Ruolo;
import it.elis.sicilianaturalmente.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Account account) {
        account.setRuolo(Ruolo.ROLE_CLIENT);
        return ResponseEntity.ok(accountService.signup(account));
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.signin(account.getEmail(), account.getPassword()));
    }

}
