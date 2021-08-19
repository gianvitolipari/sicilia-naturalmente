package it.elis.sicilianaturalmente.model;

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
public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "idAccount")
        private Long idAccount;

        @Column(name = "email")
        private String email;

        @Column(name = "nome")
        private String nome;

        @Column(name = "cognome")
        private String cognome;

        @Column(name = "password")
        private String password;

        @Column(name = "numeroTel")
        private String numeroTel;

        @Column(name = "customer_id")
        private String customer_id;

        @Enumerated(EnumType.STRING)
        @Column(name = "ruolo")
        private Ruolo ruolo;

        @ManyToMany( cascade = {CascadeType.ALL})
        private List<Prodotto> favoriti;

        @Column(name = "indirizzo")
        private String indirizzo;

        @Column(name = "civico")
        private Integer civico;

        @Column(name = "CAP")
        private String cap;

        @OneToMany( cascade = {CascadeType.ALL})
        private List<Ordine> ordini;
}
