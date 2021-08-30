package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.*;
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
public class OrdineTest {

    @Autowired
    OrdineService ordineService;

    @Autowired
    ProdottoRepository prodottoRepository;

    @Autowired
    OrdineRepository ordineRepository;

    private final String titolo = "test";
    private final String defaultPassword = "test1234";
    private final String defaultName = "test";

    @BeforeEach
    public void beforeEach() {


    }

    @Test
    void getContenutoOrdine() {
        Prodotto prodotto = new Prodotto().setTitolo(titolo).setQuantita("2").setPrezzo(3F);

        if(!prodottoRepository.findByTitolo(titolo).isPresent()) {
            prodottoRepository.save(prodotto.setDeleted(true));
        }

        //prodottoRepository.save(prodotto.setDeleted(false));

        HashSet<String> titoloProdotto = prodottoRepository.findByTitolo(titolo).stream().map(Prodotto::getTitolo).collect(Collectors.toCollection(HashSet::new));

        assertEquals(Set.of(prodotto.getTitolo()),titoloProdotto);
    }

}
