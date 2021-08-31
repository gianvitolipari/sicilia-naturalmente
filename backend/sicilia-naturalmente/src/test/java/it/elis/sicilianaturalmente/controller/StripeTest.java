package it.elis.sicilianaturalmente.controller;

import com.stripe.model.Customer;
import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.PaymentMethodData;
import it.elis.sicilianaturalmente.model.Ruolo;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import it.elis.sicilianaturalmente.service.AccountService;
import it.elis.sicilianaturalmente.service.StripeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
public class StripeTest {
    @MockBean
    private StripeService stripeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TestRestTemplate restTemplate;

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
    public void getCustomerId() throws Exception {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setName(nome);
        customer.setId("test");
        given(stripeService.createCustomer()).willReturn(customer);

        ResponseEntity<String> response;
        response = restTemplate.exchange("/payment/customer_id", HttpMethod.GET, new HttpEntity<>(null, headers), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customer.getId(), response.getBody());
    }

    @Test
    public void getCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setName(nome);
        customer.setId("test");
        given(stripeService.getCustomer()).willReturn(customer);

        ResponseEntity<String> response;
        response = restTemplate.exchange("/payment/customer", HttpMethod.GET, new HttpEntity<>(null, headers), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customer.toString(), response.getBody());
    }

    @Test
    public void getPaymentMethodCollection() throws Exception {
        String responseText="[{\"last4\":\"4242\",\"paymentMethodId\":\"metodoDiPagamentoTest\"}]";
        List<PaymentMethodData> paymentMethodData = new ArrayList<>();
        paymentMethodData.add(new PaymentMethodData().setPaymentMethodId("metodoDiPagamentoTest").setLast4("4242"));
        given(stripeService.getPaymentMethod()).willReturn(paymentMethodData);

        ResponseEntity<String> response;
        response = restTemplate.exchange("/payment/customer_payment_methods", HttpMethod.GET, new HttpEntity<>(null, headers), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseText, response.getBody());
    }

}
