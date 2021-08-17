package it.elis.sicilianaturalmente.service;

import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import it.elis.sicilianaturalmente.model.PaymentData;

public interface StripeService {
    public Customer createCustomer() throws Exception;
    public Charge chargeNewCard(String token, double amount) throws Exception;
    public Charge chargeCustomerCard(String customerId, int amount) throws Exception;
    public PaymentIntent createPaymentIntent(PaymentData paymentData) throws Exception;
}
