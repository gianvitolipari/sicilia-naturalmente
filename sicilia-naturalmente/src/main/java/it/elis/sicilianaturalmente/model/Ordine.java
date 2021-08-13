package it.elis.sicilianaturalmente.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
@Accessors(chain = true)
public class Ordine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrdine")
    private Long idOrdine;

    @Column(name = "data")
    private Date data;

    @Column(name = "prezzoTot")
    private Float prezzoTot;

    @ManyToMany( cascade = {CascadeType.ALL})
    private List<Prodotto> prodotti;

    @Column(name = "stato")
    private Stato stato;


    @ManyToOne( cascade = {CascadeType.ALL})
    @JoinColumn(name = "idUtente", referencedColumnName = "idAccount")
    private Account idAccount;

}
