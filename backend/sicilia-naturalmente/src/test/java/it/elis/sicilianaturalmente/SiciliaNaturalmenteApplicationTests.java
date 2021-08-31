package it.elis.sicilianaturalmente;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Prodotto;
import it.elis.sicilianaturalmente.model.Ruolo;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import it.elis.sicilianaturalmente.repository.ProdottoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.transaction.Transactional;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@Transactional
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class SiciliaNaturalmenteApplicationTests {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Test
    @Rollback(value = false)
    void insertTest() {
        accountRepository.save(
            new Account()
                .setNome("Gianvito")
                .setCognome("Grassi")
                .setEmail("gianvito99.gg@gmail.com")
                .setPassword(passwordEncoder.encode("123456"))
                .setRuolo(Ruolo.ROLE_ADMIN)
        );
    }

    @Test
    @Rollback(value = false)
    void insertTestProduct(){
        prodottoRepository.save(
            new Prodotto()
                .setImmagine("immagine")
                .setDescrizione("descrizione")
                .setImmagineRetro("immagineRetro")
                .setMinutiPreparazione(5)
                .setTitolo("titolo")
                .setPrezzo(1F)
                .setQuantita(String.valueOf(1))
        );
    }
}
