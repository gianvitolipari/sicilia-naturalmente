package it.elis.sicilianaturalmente.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import static it.elis.sicilianaturalmente.util.Regex.VALID_INT_NUMBER;
import static it.elis.sicilianaturalmente.util.Regex.VALID_ONLY_LETTERS;

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
    private String data;

    @Column(name = "prezzoTot")
    private Long prezzoTot;

    @Enumerated(EnumType.STRING)
    @Column(name = "statoPagamento")
    private StatoPagamento statoPagamento;

   /* @OneToMany
    @Column(name = "ordineProdotti ")
    private List<OrdineProdotti> ordineProdotti;

    */

    @Enumerated(EnumType.STRING)
    @Column(name = "stato")
    private Stato stato;




    /*@ManyToOne( cascade = {CascadeType.ALL})
    @JoinColumn(name = "idUtente", referencedColumnName = "idAccount")
    private Account idAccount;
     */

    public static RegexData validateIdOrdine(Long idOrdine){
        RegexData regexData = new RegexData().setValid(true);
        Matcher validate;

        if(idOrdine==null){
            regexData.setValid(false)
                    .setError("The idOrdine field has not been filled in with a correct format");
            return regexData;
        }
        validate = VALID_INT_NUMBER.matcher(idOrdine.toString());
        if(!validate.find()){
            regexData.setValid(validate.find())
                    .setError("The idOrdine field has not been filled in with a correct format");
            return regexData;
        }else{
            return regexData;
        }
    }

}
