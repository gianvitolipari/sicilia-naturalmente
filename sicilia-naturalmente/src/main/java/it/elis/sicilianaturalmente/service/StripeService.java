package it.elis.sicilianaturalmente.service;

import com.stripe.model.Charge;
import com.stripe.model.Customer;

public interface StripeService {
    public Customer createCustomer(String token, String email) throws Exception;
    public Charge chargeNewCard(String token, double amount) throws Exception;
    public Charge chargeCustomerCard(String customerId, int amount) throws Exception;
}
