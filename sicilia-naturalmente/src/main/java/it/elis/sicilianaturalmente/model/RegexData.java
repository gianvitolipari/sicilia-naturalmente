package it.elis.sicilianaturalmente.model;

import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
@Accessors(chain = true)
public class RegexData {
    private boolean valid;
    private String error;
}
