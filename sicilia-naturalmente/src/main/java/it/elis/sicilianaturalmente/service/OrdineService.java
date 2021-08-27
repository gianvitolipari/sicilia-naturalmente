package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.Ordine;
import it.elis.sicilianaturalmente.model.PaymentData;

public interface OrdineService {
    public Ordine createOrder(PaymentData paymentData);
}
