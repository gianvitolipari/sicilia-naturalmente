package it.elis.sicilianaturalmente.repository;

import it.elis.sicilianaturalmente.model.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {
}
