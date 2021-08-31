package it.elis.sicilianaturalmente.controller;

import it.elis.sicilianaturalmente.model.*;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import it.elis.sicilianaturalmente.repository.ProdottoRepository;
import it.elis.sicilianaturalmente.security.JwtTokenProvider;
import it.elis.sicilianaturalmente.service.AccountService;
import it.elis.sicilianaturalmente.service.OrdineService;
import it.elis.sicilianaturalmente.service.ProdottoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

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
public class UserTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    AccountService accountService;

    @Autowired
    ProdottoService prodottoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    OrdineService ordineService;

    @Autowired
    AccountRepository accountRepository;

    private final HttpHeaders headers = new HttpHeaders();

    private final String email = "test@gmail.com";
    private final String emailRiserva ="test2@gmail.com";
    private final String password = "test1234";
    private final String nome = "test";

    @BeforeEach
    public void beforeEach() {

        accountRepository.deleteAll();
        accountRepository.save(new Account().setPassword(passwordEncoder.encode(password)).setNome(nome).setEmail(email).setRuolo(Ruolo.ROLE_ADMIN));
        headers.clear();
        String token = accountService.signin(email,password);
        headers.add("Authorization", "Bearer " + token);
    }

    @Test
    public void delete() {
        String responseText="Cancellazione dell'Account " + email + " effettuata correttamente";
        ResponseEntity<String> response;
        response = restTemplate.exchange("/users/delete?email={email}", HttpMethod.DELETE, new HttpEntity<>(null, headers), String.class,email);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseText, response.getBody());
    }

    @Test
    public void changeRole() {
        String responseText="Permission changed successfully";
        ResponseEntity<String> response;
        response = restTemplate.exchange("/users/role?email={email}", HttpMethod.POST, new HttpEntity<>(null, headers), String.class,emailRiserva);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseText, response.getBody());
    }

    @Test
    public void changeAddressInformation() {
        String responseText="The address information has been successfully changed";

        ResponseEntity<String> response;
        response = restTemplate.exchange("/users/address?indirizzo={indirizzo}", HttpMethod.POST, new HttpEntity<>(null, headers), String.class,"indirizzo");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseText, response.getBody());
    }

    @Test
    public void getContenutoOrdine() {
        String responseText="[{\"titolo\":\"test\",\"quantita\":2}]";
        List<ContenutoProdotto> contenutoProdottos= new ArrayList<>();
        contenutoProdottos.add(new ContenutoProdotto().setTitolo("test").setQuantita(2L));
        given(ordineService.getContenutoOrdine(any())).willReturn(contenutoProdottos);


        ResponseEntity<String> response;
        response = restTemplate.exchange("/users/order/content/{idOrdine}", HttpMethod.GET, new HttpEntity<>(null, headers), String.class,2L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseText, response.getBody());
    }

    @Test
    public void getOrders() {
        String responseText="The order status was successfully changed";
        List<Ordine> ordini= new ArrayList<>();
        ordini.add(new Ordine().setIdOrdine(2L));
        given(ordineService.getOrders(any())).willReturn(ordini);


        ResponseEntity<String> response;
        response = restTemplate.exchange("/users/order/status?idOrdine={idOrdine}&statoPagamento={statoPagamento}", HttpMethod.POST, new HttpEntity<>(null, headers), String.class, 2L, Stato.CONSEGNATO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseText, response.getBody());
    }

}
