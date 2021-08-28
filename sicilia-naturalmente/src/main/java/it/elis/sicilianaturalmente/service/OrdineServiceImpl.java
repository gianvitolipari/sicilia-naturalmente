package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.exception.CustomException;
import it.elis.sicilianaturalmente.model.*;
import it.elis.sicilianaturalmente.repository.OrdineProdottoRepository;
import it.elis.sicilianaturalmente.repository.OrdineRepository;
import it.elis.sicilianaturalmente.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrdineServiceImpl implements OrdineService{
    @Autowired
    OrdineRepository ordineRepository;

    @Autowired
    ProdottoRepository prodottoRepository;

    @Autowired
    OrdineProdottoRepository ordineProdottoRepository;

    @Autowired
    AccountService accountService;


    @Override
    public Ordine createOrder(PaymentData paymentData) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        Ordine ordine =new Ordine().setData(currentTime)
                .setPrezzoTot(paymentData.getPrice())
                .setStatoPagamento(StatoPagamento.NON_PAGATO)
                .setStato(Stato.IN_PREPARAZIONE);
        ordineRepository.save(ordine);

        List<OrdineProdotti> prodotti = new ArrayList<>();
        Prodotto prodotto = new Prodotto();
        Long quantita;
        for (int i=0; i<paymentData.getProducts().size();i++) {
            prodotto = prodottoRepository.findByTitolo(paymentData.getProducts().get(i).getTitolo()).get();
            quantita = Long.valueOf(paymentData.getProducts().get(i).getQuantita());
            OrdineProdotti ordineProdotti = new OrdineProdotti().setProdotto(prodotto).setQuantita(quantita);
            prodotti.add(ordineProdotti);
            ordineProdotti.setOrdine(ordine);
            ordineProdottoRepository.save(ordineProdotti);

        }

        return ordine;
    }

    @Override
    public List<ContenutoProdotto> getContenutoOrdine(Long idOrdine) {
        Optional<Ordine> ordine = ordineRepository.findById(idOrdine);
        if(ordine == null){
            throw new CustomException("There is no order with this id", HttpStatus.NOT_FOUND);
        }
        if(isOrderUser(idOrdine)){
            List<OrdineProdotti> ordineList = ordineProdottoRepository.findByOrdine(ordine.get());
            List<ContenutoProdotto> contenutoProdotto = new ArrayList<>();
            for(int i =0; i<ordineList.size();i++){
                ContenutoProdotto prodotto=new ContenutoProdotto();
                prodotto.setTitolo(ordineList.get(i).getProdotto().getTitolo())
                        .setQuantita(ordineList.get(i).getQuantita());
                contenutoProdotto.add(prodotto);
            }
            return contenutoProdotto;
        }
        throw new CustomException("The user does not have an order with this id", HttpStatus.NOT_FOUND);
    }

    public boolean isOrderUser(Long idOrdine){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Account account = accountService.whoami(request);
        List<Ordine> ordini = account.getOrdini();
        for (Ordine o:ordini) {
            if(o.getIdOrdine()==idOrdine){
                return true;
            }
        }
        return false;
    }
}
