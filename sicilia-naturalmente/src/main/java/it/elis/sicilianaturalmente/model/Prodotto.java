package it.elis.sicilianaturalmente.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
@Accessors(chain = true)
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProdotto")
    private Long idProdotto;

    @Column(name = "titolo",unique=true,nullable = false)
    private String titolo;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "immagine",columnDefinition="LONGTEXT")
    private String immagine;

    @Column(name = "immagineRetro",columnDefinition="LONGTEXT")
    private String immagineRetro;

    @Column(name = "prezzo",nullable = false)
    private Float prezzo;

    @Column(name = "quantità",nullable = false)
    private String quantita;

    @Column(name = "grano")
    private String grano;

    @Column(name = "minutiPreparazione")
    private Integer minutiPreparazione;

    @Enumerated(EnumType.STRING)
    @Column(name = "formato")
    private Formato formato;

    @Column(name = "deleted")
    private Boolean deleted;

}
