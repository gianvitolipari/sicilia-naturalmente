package it.elis.sicilianaturalmente.model;

import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
@Accessors(chain = true)
public class PaymentMethodData {
    private String last4;
    private String paymentMethodId;
}
