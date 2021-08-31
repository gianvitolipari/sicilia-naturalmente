package it.elis.sicilianaturalmente.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
@Accessors(chain = true)
public class OrdineProdotti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrdineProdotto")
    private Long idOrdineProdotto;

    @OneToOne
    private Prodotto prodotto;

    private Long quantita;

    @ManyToOne
    private Ordine ordine;
}
