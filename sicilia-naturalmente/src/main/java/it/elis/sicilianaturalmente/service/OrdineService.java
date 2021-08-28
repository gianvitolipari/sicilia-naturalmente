package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.ContenutoProdotto;
import it.elis.sicilianaturalmente.model.Ordine;
import it.elis.sicilianaturalmente.model.OrdineProdotti;
import it.elis.sicilianaturalmente.model.PaymentData;

import java.util.List;

public interface OrdineService {
    public Ordine createOrder(PaymentData paymentData);
    public List<ContenutoProdotto> getContenutoOrdine(Long idOrdine);
}
