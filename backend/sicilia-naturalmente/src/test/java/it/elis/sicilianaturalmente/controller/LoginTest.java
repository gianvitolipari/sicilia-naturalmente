package it.elis.sicilianaturalmente.controller;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Ruolo;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import it.elis.sicilianaturalmente.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class LoginTest {

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    private AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders headers = new HttpHeaders();

    private final String email = "gianvito@gmail.com";
    private final String password = "test1234";
    private final String nome = "Gianvito";
/*
    @BeforeEach
    public void beforeEach() {
        accountRepository.deleteAll();

        String email = "gianvito99.gg@gmail.com";
        String password = "test1234";
        Account account = accountRepository.save(new Account().setEmail(email).setPassword(passwordEncoder.encode(password)).setNome("Gianvito").setCognome("Grassi").setRuolo(Ruolo.ROLE_ADMIN));

        headers.clear();
        String token = accountService.signin(email, password);
        headers.add("Authorization", "Bearer " + token);
    }
/*
    @Test
    public void getProfile() {
        // Test 403

        ResponseEntity<String> response403 =
                restTemplate.exchange(
                        "/api/professor/profile", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response403.getStatusCode());

        // Test 200

        Professor professor = new Professor(1L, 1L, "Professor name", "Professor surname", null);
        given(professorService.getProfile()).willReturn(professor);

        ResponseEntity<Professor> response200 =
                restTemplate.exchange(
                        "/api/professor/profile", HttpMethod.GET, new HttpEntity<>(null, headers), Professor.class);

        assertEquals(HttpStatus.OK, response200.getStatusCode());
        assertNotNull(response200.getBody());
        assertEquals(professor, response200.getBody());
    }

 */
@Test
void signup() {
    String token = "test";
    given(accountService.signup(any())).willReturn(token);
    Account account = new Account();

    ResponseEntity<String> response =
            restTemplate.exchange(
                    "/login/signup", HttpMethod.POST, new HttpEntity<>(account), String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(token, response.getBody());
}

    @Test
    void signin() {
        String token = "test";
        given(accountService.signin(any(), any())).willReturn(token);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "/login/signin?email={email}&password={password}", HttpMethod.POST, null,
                        String.class, email, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody());
    }

    @Test
    void passwordRecovery() {
        String responseText = "A password recovery e-mail has been sent";
        given(accountService.passwordRecovery(any())).willReturn(responseText);


        ResponseEntity<String> response =
                restTemplate.exchange(
                        "/forgotten?email={email}", HttpMethod.POST, null,
                        String.class, email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseText, response.getBody());
    }
}
