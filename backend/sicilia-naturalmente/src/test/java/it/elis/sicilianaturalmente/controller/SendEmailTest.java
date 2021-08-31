package it.elis.sicilianaturalmente.controller;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Email;
import it.elis.sicilianaturalmente.model.Ruolo;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import it.elis.sicilianaturalmente.service.AccountService;
import it.elis.sicilianaturalmente.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SendEmailTest {

    @MockBean
    private EmailService emailService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final HttpHeaders headers = new HttpHeaders();

    private final String email = "test@gmail.com";
    private final String password = "test1234";
    private final String nome = "test";

    @BeforeEach
    public void beforeEach() {
        accountRepository.deleteAll();

        accountRepository.save(
            new Account()
                .setEmail(email)
                .setPassword(passwordEncoder.encode(password))
                .setRuolo(Ruolo.ROLE_ADMIN)
                .setNome(nome)
                .setCognome(nome)
        );

        headers.clear();
        String token = accountService.signin(email, password);
        headers.add("Authorization", "Bearer " + token);
    }

    @Test
    public void sendEmail() {
        String responseText="Email correctly sent";
        Email email = new Email().setMessage("messaggioTest").setSubject("OggettoTest").setToEmail("gianvito99.gg@gmail.com");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("email", email);
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        HttpEntity<Email> httpEntity = new HttpEntity<>(email,headers);

        ResponseEntity<String> response;
        response = restTemplate.exchange("/sendEmail", HttpMethod.POST, httpEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseText, response.getBody());
    }
}
