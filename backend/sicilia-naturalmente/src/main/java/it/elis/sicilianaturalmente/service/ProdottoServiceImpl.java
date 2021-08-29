package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.exception.CustomException;
import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Formato;
import it.elis.sicilianaturalmente.model.Prodotto;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import it.elis.sicilianaturalmente.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdottoServiceImpl implements ProdottoService{
    @Autowired
    ProdottoRepository prodottoRepository;

    @Autowired
    AccountService accountService;

    @Override
    public Prodotto getProductByTitolo(String titolo) {
        Optional<Prodotto> prodotto = prodottoRepository.findByTitolo(titolo);
        if(prodotto.isEmpty() || prodotto.get().getDeleted()){
            throw new CustomException("Product not found", HttpStatus.NOT_FOUND);
        }
        return prodottoRepository.findByTitolo(titolo).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public List<Prodotto> getAllProduct() {
        List<Prodotto> prodotti = prodottoRepository.findAll();
        if(prodotti.isEmpty()){
            throw new CustomException("Products not found", HttpStatus.NOT_FOUND);
        }
        List<Prodotto> prodottiDisponibili = new ArrayList<>();
        for (Prodotto p: prodotti) {
            if(!p.getDeleted()){
                prodottiDisponibili.add(p);
            }
        }
        return prodottiDisponibili;
    }

    @Override
    public void createProduct(Prodotto prodotto) {
        if(prodotto.getTitolo()==null || prodotto.getPrezzo()==null || prodotto.getQuantita()==null){
            throw new CustomException("Check that you have entered all the required fields", HttpStatus.BAD_REQUEST);
        }
        Optional<Prodotto> newProdotto = prodottoRepository.findByTitolo(prodotto.getTitolo());
        if(!newProdotto.isEmpty() && newProdotto.get().getDeleted()==false){
            throw new CustomException("The product already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        prodotto.setDeleted(false)
                .setIdProdotto(newProdotto.get().getIdProdotto());
        prodottoRepository.save(prodotto);
    }

    @Override
    public void deleteProduct(String titolo) {
        if(titolo==null){
            throw new CustomException("Please enter a valid title", HttpStatus.BAD_REQUEST);
        }
        Optional<Prodotto> prodotto = prodottoRepository.findByTitolo(titolo);
        if(prodotto.isEmpty() || prodotto.get().getDeleted()){
            throw new CustomException("There is no product with this idProduct", HttpStatus.NOT_FOUND);
        }
        prodotto.get().setDeleted(true);
        prodottoRepository.save(prodotto.get());
    }

    @Override
    public List<Prodotto> getFormato(Formato formato) {
        if(prodottoRepository.findAllByFormato(formato).isEmpty()){
            throw new CustomException("There is no products", HttpStatus.NOT_FOUND);
        }
        List<Prodotto> prodotti = prodottoRepository.findAllByFormato(formato);
        List<Prodotto> prodottiDisponibili = new ArrayList<>();
        for (Prodotto p: prodotti) {
            if(!p.getDeleted()){
                prodottiDisponibili.add(p);
            }
        }
        return prodottiDisponibili;
    }

    @Override
    public List<Prodotto> orderByPrice() {
        if(prodottoRepository.findAll().isEmpty()){
            throw new CustomException("There is no products", HttpStatus.NOT_FOUND);
        }
        List<Prodotto> prodotti = prodottoRepository.findByOrderByPrezzoAsc();
        List<Prodotto> prodottiDisponibili = new ArrayList<>();
        for (Prodotto p: prodotti) {
            if(!p.getDeleted()){
                prodottiDisponibili.add(p);
            }
        }
        return prodottiDisponibili;
    }

    @Override
    public List<Prodotto> getByRegex(String titolo) {
        if(titolo != null){
            List<Prodotto> prodotti = prodottoRepository.findByTitoloOrderByTitoloAsc(titolo);
            List<Prodotto> prodottiDisponibili = new ArrayList<>();
            for (Prodotto p: prodotti) {
                if(!p.getDeleted()){
                    prodottiDisponibili.add(p);
                }
            }
            return prodottiDisponibili;
        }

        return getAllProduct();
    }

    @Override
    public boolean checkAvailability(String titolo, Long quantita) {
        Optional<Prodotto> prodotto = prodottoRepository.findByTitolo(titolo);
        Long quantitaDisponibile = Long.valueOf(prodotto.get().getQuantita());
        if(quantitaDisponibile>=quantita && prodotto.get().getDeleted()==false){
            return true;
        }
        return false;
    }

    @Override
    public void updateQuantity(String titolo, Long quantita) {
        if(!checkAvailability(titolo,quantita)){
            throw new CustomException("The quantity of the product is not sufficient", HttpStatus.CONFLICT);
        }
        Optional<Prodotto> prodotto = prodottoRepository.findByTitolo(titolo);
        Long quantitaDisponibile = Long.valueOf(prodotto.get().getQuantita());
        String newQuantita = String.valueOf(quantitaDisponibile-quantita);
        prodotto.get().setQuantita(newQuantita);
        prodottoRepository.deleteByTitolo(titolo);
        prodottoRepository.save(prodotto.get());
    }

    @Override
    public void changeProduct(String titolo,Float prezzo,String quantita) {
        Optional<Prodotto> prodottoOptional = prodottoRepository.findByTitolo(titolo);
        if(prodottoOptional==null){
            throw new CustomException("There is no product with this idProduct", HttpStatus.NOT_FOUND);
        }
        Prodotto prodotto1= prodottoOptional.get();
        prodotto1.setQuantita(quantita)
                .setPrezzo(prezzo);
        prodottoRepository.save(prodotto1);
    }

}
