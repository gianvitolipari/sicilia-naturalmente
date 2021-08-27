package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.*;
import it.elis.sicilianaturalmente.repository.OrdineProdottoRepository;
import it.elis.sicilianaturalmente.repository.OrdineRepository;
import it.elis.sicilianaturalmente.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrdineServiceImpl implements OrdineService{
    @Autowired
    OrdineRepository ordineRepository;

    @Autowired
    ProdottoRepository prodottoRepository;

    @Autowired
    OrdineProdottoRepository ordineProdottoRepository;


    @Override
    public Ordine createOrder(PaymentData paymentData) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        List<OrdineProdotti> prodotti = new ArrayList<>();
        Prodotto prodotto = new Prodotto();
        Long quantita;
        for (int i=0; i<paymentData.getProducts().size();i++) {
            prodotto = prodottoRepository.findByTitolo(paymentData.getProducts().get(i).getTitolo()).get();
            quantita = Long.valueOf(paymentData.getProducts().get(i).getQuantita());
            OrdineProdotti ordineProdotti = new OrdineProdotti().setProdotto(prodotto).setQuantita(quantita);
            prodotti.add(ordineProdotti);
            ordineProdottoRepository.save(ordineProdotti);

        }
        Ordine ordine =new Ordine().setData(currentTime)
                .setPrezzoTot(paymentData.getPrice())
                .setStatoPagamento(StatoPagamento.NON_PAGATO)
                .setStato(Stato.IN_PREPARAZIONE)
                .setOrdineProdotti(prodotti);
        ordineRepository.save(ordine);

        return ordine;
    }
}
