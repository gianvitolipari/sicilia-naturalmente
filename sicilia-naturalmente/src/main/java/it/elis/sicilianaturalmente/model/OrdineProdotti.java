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

    @OneToOne( cascade = {CascadeType.ALL})
    private Prodotto prodotto;

    private Long quantita;

    @ManyToOne( cascade = {CascadeType.ALL})
    private Ordine ordine;
}
