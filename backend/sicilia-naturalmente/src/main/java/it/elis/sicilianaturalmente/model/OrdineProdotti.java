package it.elis.sicilianaturalmente.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

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

    @OneToOne(cascade = {CascadeType.MERGE})
    private Prodotto prodotto;

    private Long quantita;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Ordine ordine;
}
