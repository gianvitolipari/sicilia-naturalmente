package it.elis.sicilianaturalmente;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Prodotto;
import it.elis.sicilianaturalmente.model.Ruolo;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import it.elis.sicilianaturalmente.repository.ProdottoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class SiciliaNaturalmenteApplicationTests {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ProdottoRepository prodottoRepository;

    @Test
    void contextLoads() {
    }
    @Rollback(value = false)
    @Test
    void insertTest() {

        accountRepository.save(new Account().setNome("Gianvito").setCognome("Grassi").setEmail("gianvito99.gg@gmail.com").setPassword(passwordEncoder.encode("1234")).setRuolo(Ruolo.ROLE_ADMIN));
    }

    @Rollback(value = false)
    @Test
    void insertTestProduct(){
        prodottoRepository.save(new Prodotto().setImmagine("immagine").setDescrizione("descrizione").setImmagineRetro("immagineRetro").setMinutiPreparazione(5).setTitolo("titolo"));
    }
}
