package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.Formato;
import it.elis.sicilianaturalmente.model.Prodotto;

import java.util.List;

public interface ProdottoService {
    public Prodotto getProductByTitolo(String titolo);
    public List<Prodotto> getAllProduct();
    public void createProduct(Prodotto prodotto);
    public void deleteProduct(String titolo);
    public List<Prodotto> getFormato(Formato formato);
    public List<Prodotto> orderByPrice();
    public List<Prodotto> getByRegex(String titolo);
    public boolean checkAvailability(String titolo, Long quantita);
    public void updateQuantity(String titolo,Long quantita);
    public void changeProduct(String titolo,Float prezzo,String quantita);
}
