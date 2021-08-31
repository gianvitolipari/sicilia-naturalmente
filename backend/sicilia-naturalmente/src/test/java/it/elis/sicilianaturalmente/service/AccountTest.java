package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Prodotto;
import it.elis.sicilianaturalmente.model.Ruolo;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import it.elis.sicilianaturalmente.repository.ProdottoRepository;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Autowired
    ProdottoRepository prodottoRepository;

    private Account account;
    private final String email = "email.test@mail.com";
    private final String defaultPassword = "test1234";
    private final String defaultName = "test";
    private final String titolo = "titoloTest";

    @BeforeEach
    public void beforeEach() {
       accountRepository.deleteAll();
       prodottoRepository.deleteAll();
    }

    @Test
    void signup() {
        Account account = new Account().setEmail(email).setNome(defaultName).setPassword(passwordEncoder.encode(defaultPassword)).setRuolo(Ruolo.ROLE_ADMIN);
        accountService.signup(account);
        assertEquals(1, accountRepository.findAll().size());
    }

    @Test
    void signin() {
        Account account = new Account().setEmail(email).setNome(defaultName).setPassword(passwordEncoder.encode(defaultPassword)).setRuolo(Ruolo.ROLE_ADMIN);
        accountRepository.save(account);
        String token = accountService.signin(email, defaultPassword);
        assertNotNull(token);
    }

    @Test
    void changeRole() {
        Account account = new Account().setEmail(email).setNome(defaultName).setPassword(passwordEncoder.encode(defaultPassword)).setRuolo(Ruolo.ROLE_ADMIN);
        Account account1 = new Account().setEmail("test2@gmail.com").setNome(defaultName).setPassword(passwordEncoder.encode(defaultPassword)).setRuolo(Ruolo.ROLE_ADMIN);

        accountRepository.save(account);
        accountRepository.save(account1);
        accountService.changeRole(email);
        assertEquals(Ruolo.ROLE_CLIENT, accountRepository.findByEmail(email).get().getRuolo());
    }

    @Test
    void deleteAccount() {
        Account account = new Account().setEmail(email).setNome(defaultName).setPassword(passwordEncoder.encode(defaultPassword)).setRuolo(Ruolo.ROLE_ADMIN);
        Account account1 = new Account().setEmail("test2@gmail.com").setNome(defaultName).setPassword(passwordEncoder.encode(defaultPassword)).setRuolo(Ruolo.ROLE_ADMIN);

        accountRepository.save(account);
        accountRepository.save(account1);
        accountService.deleteAccount(email);
        assertEquals(1, accountRepository.findAll().size());
    }

}
