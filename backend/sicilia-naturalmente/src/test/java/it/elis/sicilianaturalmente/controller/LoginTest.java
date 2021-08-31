package it.elis.sicilianaturalmente.controller;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class LoginTest {

    @MockBean
    private AccountService accountService;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String email = "test@gmail.com";
    private final String password = "test1234";
    private final String nome = "test";

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
        ResponseEntity<String> response =
                restTemplate.exchange(
                        "/login/forgotten?email={email}", HttpMethod.POST, null, String.class, email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
