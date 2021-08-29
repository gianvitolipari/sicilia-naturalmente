package it.elis.sicilianaturalmente.repository;

import it.elis.sicilianaturalmente.model.Formato;
import it.elis.sicilianaturalmente.model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProdottoRepository extends JpaRepository<Prodotto,Long> {
    Optional<Prodotto> findByTitolo(String titolo);
    List<Prodotto> findAll();
    @Transactional
    void deleteByTitolo(String titolo);
    Optional<Prodotto> findByIdProdotto(Long idProdotto);
    List<Prodotto> findAllByFormato(Formato formato);
    List<Prodotto> findByOrderByPrezzoAsc();
    @Query(nativeQuery=true, value="SELECT * FROM prodotto p where regexp_like(p.titolo, ?1)")
    List<Prodotto> findByTitoloOrderByTitoloAsc(String titolo);
}
