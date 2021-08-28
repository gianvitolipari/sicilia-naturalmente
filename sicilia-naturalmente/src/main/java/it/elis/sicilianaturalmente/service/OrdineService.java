package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.*;

import java.util.List;

public interface OrdineService {
    public Ordine createOrder(PaymentData paymentData);
    public List<ContenutoProdotto> getContenutoOrdine(Long idOrdine);
    public List<Ordine> getOrders(Account account);
    public void changeStatus(Ordine ordine);
}
