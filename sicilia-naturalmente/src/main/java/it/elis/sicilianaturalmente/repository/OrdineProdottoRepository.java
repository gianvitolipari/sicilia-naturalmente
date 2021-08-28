package it.elis.sicilianaturalmente.repository;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Ordine;
import it.elis.sicilianaturalmente.model.OrdineProdotti;
import it.elis.sicilianaturalmente.model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdineProdottoRepository extends JpaRepository<OrdineProdotti,Long> {
   public List<OrdineProdotti> findByOrdine(Ordine ordine);
}
