package it.elis.sicilianaturalmente.controller;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Formato;
import it.elis.sicilianaturalmente.model.Prodotto;
import it.elis.sicilianaturalmente.model.Ruolo;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import it.elis.sicilianaturalmente.service.AccountService;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ProductTest {

    @MockBean
    private ProdottoService prodottoService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestRestTemplate restTemplate;

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
    public void getProducts() {
        List<Prodotto> products = new ArrayList<>();
        products.add(new Prodotto().setTitolo("titolo").setPrezzo(3.0F).setQuantita("2"));
        given(prodottoService.getAllProduct()).willReturn(products);

        ResponseEntity<List<Prodotto>> response;
        response = restTemplate.exchange("/product/", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(products, response.getBody());
    }

    @Test
    public void getProduct() {
        Prodotto prodotto = new Prodotto().setTitolo("titolo").setPrezzo(3.0F).setQuantita("2");
        given(prodottoService.getProductByTitolo(prodotto.getTitolo())).willReturn(prodotto);

        ResponseEntity<Prodotto> response;
        response = restTemplate.exchange("/product/{id}", HttpMethod.GET, null, Prodotto.class, prodotto.getTitolo());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(prodotto, response.getBody());
    }

    @Test
    public void createProduct() {
        String responseText="Product creation successful";
        Prodotto prodotto = new Prodotto().setTitolo("titolo").setPrezzo(3.0F).setQuantita("2");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Prodotto> httpEntity = new HttpEntity<>(prodotto,headers);

        ResponseEntity<String> response;
        response = restTemplate.exchange("/product/create", HttpMethod.POST, httpEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseText, response.getBody());
    }

    @Test
    public void deleteProduct() {
        String responseText="Product deleted successfully";
        Prodotto p = new Prodotto().setTitolo("titolo");
        ResponseEntity<String> response;
        response = restTemplate.exchange("/product/delete/{titolo}", HttpMethod.DELETE, new HttpEntity<>(null, headers), String.class,p.getTitolo());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseText, response.getBody());
    }

    @Test
    public void getFormato() {
        List<Prodotto> product = new ArrayList<>();
        Prodotto prodotto = new Prodotto().setTitolo("titolo").setPrezzo(3.0F).setQuantita("2").setFormato(Formato.CORTA);
        product.add(prodotto);
        given(prodottoService.getFormato(prodotto.getFormato())).willReturn(product);

        ResponseEntity<List<Prodotto>> response;
        response = restTemplate.exchange("/product/format/{formato}", HttpMethod.GET, null, new ParameterizedTypeReference<>() {} ,prodotto.getFormato());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(product, response.getBody());
    }

    @Test
    public void orderByPrice() {
        List<Prodotto> product = new ArrayList<>();
        Prodotto prodotto = new Prodotto().setTitolo("titolo").setPrezzo(3.0F).setQuantita("2");
        product.add(prodotto);
        given(prodottoService.orderByPrice()).willReturn(product);

        ResponseEntity<List<Prodotto>> response;
        response = restTemplate.exchange("/product/price", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(product, response.getBody());
    }

    @Test
    public void searchProduct() {
        List<Prodotto> product = new ArrayList<>();
        Prodotto prodotto = new Prodotto().setTitolo("titolo").setPrezzo(3.0F).setQuantita("2");
        product.add(prodotto);
        given(prodottoService.getByRegex(prodotto.getTitolo())).willReturn(product);

        ResponseEntity<List<Prodotto>> response;
        response = restTemplate.exchange("/product/research/{titolo}", HttpMethod.GET, null, new ParameterizedTypeReference<>() {},prodotto.getTitolo());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(product, response.getBody());
    }

    @Test
    public void changeProduct() {
        String responseText="Correctly modified product";
        Prodotto prodotto = new Prodotto().setTitolo("titolo").setPrezzo(3.0F).setQuantita("2");

        ResponseEntity<String> response;
        response = restTemplate.exchange("/product/editProduct?titolo={titolo}&prezzo={prezzo}&quantita={quantita}", HttpMethod.POST, new HttpEntity<>(null, headers), String.class,prodotto.getTitolo(),prodotto.getPrezzo(),prodotto.getQuantita());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseText, response.getBody());
    }
}
