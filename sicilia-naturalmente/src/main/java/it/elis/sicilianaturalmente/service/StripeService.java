package it.elis.sicilianaturalmente.service;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import it.elis.sicilianaturalmente.model.PaymentData;
import it.elis.sicilianaturalmente.model.PaymentMethodData;

import java.util.List;

public interface StripeService {
    public Customer createCustomer() throws Exception;
    public void createPaymentIntent(PaymentData paymentData) throws Exception;
    public PaymentMethod addPaymentMethod(String payment_method) throws Exception;
    public Customer getCustomer() throws Exception;
    public List<PaymentMethodData> getPaymentMethod() throws Exception;
}
