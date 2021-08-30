package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Ruolo;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AccountTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    private Account account;
    private final String email = "email.test@mail.com";
    private final String defaultPassword = "test1234";
    private final String defaultName = "test";

    @BeforeEach
    public void beforeEach() {

        if(!accountRepository.findByEmail(email).isPresent()) {
            accountRepository.save(new Account().setEmail(email).setNome(defaultName).setPassword(passwordEncoder.encode(defaultPassword)).setRuolo(Ruolo.ROLE_ADMIN));
        }
    }

    @Test
    void signup() {
        String customMail = email;
        Account account = new Account().setEmail(email).setNome(defaultName).setPassword(passwordEncoder.encode(defaultPassword)).setRuolo(Ruolo.ROLE_ADMIN);

        if(!accountRepository.findByEmail(email).isPresent()){
            accountService.signup(account);
        }

        HashSet<String> accountEmails = accountRepository.findByEmail(email).stream().map(Account::getEmail).collect(Collectors.toCollection(HashSet::new));

        assertEquals(Set.of(customMail), accountEmails);
    }

    @Test
    void signin() {
        String token = accountService.signin(email, defaultPassword);
        assertNotNull(token);
    }


}
