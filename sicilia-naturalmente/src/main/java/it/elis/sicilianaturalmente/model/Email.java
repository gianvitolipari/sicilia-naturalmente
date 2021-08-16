package it.elis.sicilianaturalmente.model;

import lombok.*;
import lombok.experimental.Accessors;

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
}
