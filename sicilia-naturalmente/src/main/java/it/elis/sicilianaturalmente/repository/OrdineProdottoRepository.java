package it.elis.sicilianaturalmente.repository;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.OrdineProdotti;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdineProdottoRepository extends JpaRepository<OrdineProdotti,Long> {
}
