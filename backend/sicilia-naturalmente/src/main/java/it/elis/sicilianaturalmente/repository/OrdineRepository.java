package it.elis.sicilianaturalmente.repository;

import it.elis.sicilianaturalmente.model.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    Optional<Ordine> findByIdOrdine(Long idOrdine);
}
