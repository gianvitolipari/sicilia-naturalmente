package it.elis.sicilianaturalmente.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.regex.Matcher;

import static it.elis.sicilianaturalmente.util.Regex.VALID_EMAIL_ADDRESS_REGEX;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
@Accessors(chain = true)
public class Email {

    private String toEmail;

    private String subject;

    private String message;

    public static RegexData validateEmail(String email){
        RegexData regexData=new RegexData().setValid(true);
        Matcher validate;
        if (email!=null){
            validate = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
            if(!validate.find()){
                regexData.setValid(validate.find())
                        .setError("The Email field has not been filled in with a correct format");
                return regexData;
            }
        }else{
            regexData.setValid(false)
                    .setError("The Email field has not been filled in with a correct format");
            return regexData;
        }
        return regexData;
    }
}
