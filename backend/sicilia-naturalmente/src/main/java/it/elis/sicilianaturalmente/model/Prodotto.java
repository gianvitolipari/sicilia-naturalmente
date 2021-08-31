package it.elis.sicilianaturalmente.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.regex.Matcher;

import static it.elis.sicilianaturalmente.util.Regex.*;

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
    @Column(name = "idProdotto", nullable = false)
    private Long idProdotto;

    @Column(name = "titolo",unique=true,nullable = false)
    private String titolo;

    @Column(name = "descrizione")
    private String descrizione;

    @Lob
    @Column(name = "immagine",columnDefinition="LONGTEXT")
    private String immagine;

    @Lob
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

    public RegexData validateProduct(){
        RegexData regexData = new RegexData().setValid(true);
        Matcher validate;
        if(this.titolo!=null){
            validate = VALID_ONLY_LETTERS.matcher(this.titolo);
            if(!validate.find()){
                regexData.setValid(validate.find())
                        .setError("The Titolo field has not been filled in with a correct format");
                return regexData;
            }
        }
        if(this.grano!=null){
            validate = VALID_LETTERS_NUMBERS.matcher(this.grano);
            if(!validate.find()){
                regexData.setValid(validate.find())
                        .setError("The Grano field has not been filled in with a correct format");
                return regexData;
            }
        }
        if(this.prezzo!=null){
            validate = VALID_FLOAT_NUMBER.matcher(this.prezzo.toString());
            if(!validate.find()){
                regexData.setValid(validate.find())
                        .setError("The Prezzo field has not been filled in with a correct format");
                return regexData;
            }
        }
        if(this.quantita!=null){
            validate = VALID_INT_NUMBER.matcher(this.quantita);
            if(!validate.find()){
                regexData.setValid(validate.find())
                        .setError("The Quantita' field has not been filled in with a correct format");
                return regexData;
            }
        }
        if(this.minutiPreparazione!=null){
            validate = VALID_INT_NUMBER.matcher(this.minutiPreparazione.toString());
            if(!validate.find()){
                regexData.setValid(validate.find())
                        .setError("The Minuti Di Preparazione' field has not been filled in with a correct format");
                return regexData;
            }
        }

        return regexData;
    }

    public static RegexData validateProductTitolo(String titolo){
        RegexData regexData = new RegexData().setValid(true);
        Matcher validate;

        if(titolo==null){
            regexData.setValid(false)
                    .setError("The Titolo field has not been filled in with a correct format");
            return regexData;
        }
        validate = VALID_ONLY_LETTERS.matcher(titolo);
        if(!validate.find()){
            regexData.setValid(validate.find())
                    .setError("The Titolo field has not been filled in with a correct format");
            return regexData;
        }else{
            return regexData;
        }
    }
}
