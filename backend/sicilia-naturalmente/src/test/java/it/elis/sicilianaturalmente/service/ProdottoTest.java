package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.Formato;
import it.elis.sicilianaturalmente.model.Prodotto;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ProdottoTest {
    @Autowired
    ProdottoService prodottoService;

    @Autowired
    OrdineProdottoRepository ordineProdottoRepository;

    @Autowired
    OrdineRepository ordineRepository;

    @Autowired
    ProdottoRepository prodottoRepository;

    private final String titolo = "titoloTest";
    private final Formato formato = Formato.CORTA;
    private final String regex = "to";
    private final String defaultName = "test";

    @BeforeEach
    public void beforeEach() {
        ordineProdottoRepository.deleteAll();
        ordineRepository.deleteAll();
        prodottoRepository.deleteAll();
    }

    @Test
    void getProductByTitolo() {
        Prodotto prodotto = new Prodotto().setTitolo(titolo).setQuantita("2").setPrezzo(3F).setDeleted(false);
        prodottoRepository.save(prodotto);

        assertEquals(prodotto,prodottoService.getProductByTitolo(titolo));
    }

    @Test
    void getAllProduct(){
        Prodotto prodotto = new Prodotto().setTitolo(titolo).setQuantita("2").setPrezzo(3F).setDeleted(false);
        prodottoRepository.save(prodotto);
        List<Prodotto> products = new ArrayList<>();
        products.add(prodotto);

        assertEquals(products,prodottoService.getAllProduct());
    }

    @Test
    void createProduct(){
        Prodotto prodotto = new Prodotto().setTitolo(titolo).setQuantita("2").setPrezzo(3F).setDeleted(false);
        prodottoService.createProduct(prodotto);

        assertEquals(1,prodottoRepository.findAll().size());
    }

    @Test
    void deleteProduct(){
        Prodotto prodotto = new Prodotto().setTitolo(titolo).setQuantita("2").setPrezzo(3F).setDeleted(false);
        prodottoService.createProduct(prodotto);
        prodottoService.deleteProduct(titolo);
        assertEquals(true,prodottoRepository.findByTitolo(titolo).get().getDeleted());

    }

    @Test
    void getFormato() {
        Prodotto prodotto = new Prodotto().setTitolo(titolo).setQuantita("2").setPrezzo(3F).setDeleted(false).setFormato(formato);
        prodottoRepository.save(prodotto);
        List<Prodotto> products = new ArrayList<>();
        products.add(prodotto);
        assertEquals(products,prodottoService.getFormato(formato));
    }

    @Test
    void updateQuantity() {
        Long quantita = 10L;
        Prodotto prodotto = new Prodotto().setTitolo(titolo).setQuantita("20").setPrezzo(3F).setDeleted(false).setFormato(formato);
        prodottoRepository.save(prodotto);
        prodottoService.updateQuantity(titolo,quantita);
        Long quantitaAggiornata = Long.valueOf(prodotto.getQuantita()) - quantita;
        assertEquals(quantitaAggiornata.toString(),prodottoRepository.findByTitolo(titolo).get().getQuantita());
    }

    @Test
    void changeProduct() {
        String quantita = "10";
        Float prezzo = 30F;
        Prodotto prodotto = new Prodotto().setTitolo(titolo).setQuantita("20").setPrezzo(3F).setDeleted(false);
        prodottoRepository.save(prodotto);
        prodottoService.changeProduct(titolo,prezzo,quantita);
        assertEquals(quantita,prodottoRepository.findByTitolo(titolo).get().getQuantita());
        assertEquals(prezzo,prodottoRepository.findByTitolo(titolo).get().getPrezzo());
    }



}
