package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.PaymentData;
import it.elis.sicilianaturalmente.model.Prodotto;
import it.elis.sicilianaturalmente.model.Stato;
import it.elis.sicilianaturalmente.repository.OrdineProdottoRepository;
import it.elis.sicilianaturalmente.repository.OrdineRepository;
import it.elis.sicilianaturalmente.repository.ProdottoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class OrdineTest {

    @Autowired
    OrdineService ordineService;

    @Autowired
    ProdottoRepository prodottoRepository;

    @Autowired
    OrdineProdottoRepository ordineProdottoRepository;

    @Autowired
    OrdineRepository ordineRepository;

    private final String titolo = "test";
    private final String defaultPassword = "test1234";
    private final String defaultName = "test";

    @BeforeEach
    public void beforeEach() {
        ordineProdottoRepository.deleteAll();
        ordineRepository.deleteAll();
        prodottoRepository.deleteAll();
    }

    @Test
    void createOreder(){

        Prodotto prodotto = new Prodotto().setQuantita("2").setPrezzo(3F).setTitolo("titoloTest").setDeleted(false);
        prodottoRepository.save(prodotto.setQuantita("4"));
        List<Prodotto> products = new ArrayList<>();
        products.add(prodotto);
        PaymentData paymentData = new PaymentData().setPaymentMethod("pm_test1234").setProducts(products).setPrice(3L);
        ordineService.createOrder(paymentData);

        assertEquals(1,ordineRepository.findAll().size());
    }

    @Test
    void changeStatus() {
        Stato stato = Stato.SPEDITO;
        Prodotto prodotto = new Prodotto().setQuantita("2").setPrezzo(3F).setTitolo("titoloTest").setDeleted(false);
        prodottoRepository.save(prodotto.setQuantita("4"));
        List<Prodotto> products = new ArrayList<>();
        products.add(prodotto);
        PaymentData paymentData = new PaymentData().setPaymentMethod("pm_test1234").setProducts(products).setPrice(3L);
        ordineService.createOrder(paymentData);
        ordineService.changeStatus(1L,stato);
        assertEquals(stato,ordineRepository.findByIdOrdine(1L).get().getStato());
    }

}
