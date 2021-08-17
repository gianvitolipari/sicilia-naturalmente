package it.elis.sicilianaturalmente.model;

import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
@Accessors(chain = true)
public class PaymentData {
    private String paymentMethod;
    private Long price;
}
