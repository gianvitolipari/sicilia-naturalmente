package it.elis.sicilianaturalmente.model;

import lombok.*;
import lombok.experimental.Accessors;

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
public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "idAccount")
        private Long idAccount;

        @Column(name = "email", unique=true,nullable = false)
        private String email;

        @Column(name = "nome",nullable = false)
        private String nome;

        @Column(name = "cognome")
        private String cognome;

        @Column(name = "password",nullable = false)
        private String password;

        @Column(name = "customer_id")
        private String customer_id;

        @Enumerated(EnumType.STRING)
        @Column(name = "ruolo")
        private Ruolo ruolo;

        @ManyToMany( cascade = {CascadeType.ALL})
        private List<Prodotto> favoriti;

        @Column(name = "indirizzo")
        private String indirizzo;

        @OneToMany( cascade = {CascadeType.ALL})
        private List<Ordine> ordini;

        public  RegexData validateAccount(){
                RegexData regexData=new RegexData().setValid(true);
                Matcher validate;
                if(this.nome!=null){
                        validate = VALID_ONLY_LETTERS.matcher(this.nome);
                        if(!validate.find()){
                                regexData.setValid(validate.find())
                                        .setError("The Name field has not been filled in with a correct format");
                                return regexData;
                        }
                }
                if (this.getEmail()!=null){
                        validate = VALID_EMAIL_ADDRESS_REGEX.matcher(this.email);
                        if(!validate.find()){
                                regexData.setValid(validate.find())
                                        .setError("The Email field has not been filled in with a correct format");
                                return regexData;
                        }
                }
                if (this.getPassword()!=null){
                        validate = VALID_MIN_AND_MAX_SIZE.matcher(this.password);
                        if(!validate.find()){
                                regexData.setValid(validate.find())
                                        .setError("The Password field has not been filled in with a correct format, min lenght 6 characters and max 25 characters");
                                return regexData;
                        }
                }
                if(this.cognome != null){
                        validate = VALID_ONLY_LETTERS.matcher(this.cognome);
                        if(!validate.find()){
                                regexData.setValid(validate.find())
                                        .setError("The Surname field has not been filled in with a correct format");
                                return regexData;
                        }
                }

                return regexData;
        }

        public static RegexData validateAddress(String indirizzo){
                RegexData regexData = new RegexData().setValid(true);
                Matcher validate;

                if(indirizzo==null){
                        regexData.setValid(false)
                                .setError("The Address field has not been filled in with a correct format");
                        return regexData;
                }
                return regexData;

        }

        public static RegexData validatePassword(String password){
                RegexData regexData = new RegexData().setValid(true);
                Matcher validate;

                if(password==null){
                        regexData.setValid(false)
                                .setError("The Password field has not been filled in with a correct format");
                        return regexData;
                }
                validate = VALID_MIN_AND_MAX_SIZE.matcher(password);
                if(!validate.find()){
                        regexData.setValid(validate.find())
                                .setError("The Password field has not been filled in with a correct format");
                        return regexData;
                }else{
                        return regexData;
                }
        }
}
