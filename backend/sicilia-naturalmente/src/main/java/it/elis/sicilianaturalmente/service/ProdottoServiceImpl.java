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

    /**
     * The method returns a product, the search is carried out by product title
     * @param titolo
     * @return Prodotto
     */
    @Override
    public Prodotto getProductByTitolo(String titolo) {
        Optional<Prodotto> prodotto = prodottoRepository.findByTitolo(titolo);
        if(prodotto.isEmpty() || prodotto.get().getDeleted()){
            throw new CustomException("Product not found", HttpStatus.NOT_FOUND);
        }
        return prodottoRepository.findByTitolo(titolo).orElseThrow(() -> new RuntimeException());
    }

    /**
     * The method returns the list of all the products present in the system
     * @return List<Prodotto>
     */
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

    /**
     * The method creates a product
     * @param prodotto
     */
    @Override
    public void createProduct(Prodotto prodotto) {
        if(prodotto.getTitolo()==null || prodotto.getPrezzo()==null || prodotto.getQuantita()==null){
            throw new CustomException("Check that you have entered all the required fields", HttpStatus.BAD_REQUEST);
        }
        Optional<Prodotto> newProdotto = prodottoRepository.findByTitolo(prodotto.getTitolo());
        if(!newProdotto.isEmpty() && newProdotto.get().getDeleted()==false){
            throw new CustomException("The product already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        prodotto.setDeleted(false);
        if(!newProdotto.isEmpty() && newProdotto.get().getDeleted()==true){
            prodotto.setIdProdotto(newProdotto.get().getIdProdotto());
        }
        prodottoRepository.save(prodotto);
    }

    /**
     * The method removes a product
     * @param titolo
     */
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

    /**
     * The method returns the list of all products associated with the format
     * @param formato
     * @return List<Prodotto>
     */
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

    /**
     * The method returns the list of products sorted in ascending order by price
     * @return List<Prodotto>
     */
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

    /**
     * The method searches for products through a regex
     * @param titolo
     * @return List<Prodotto>
     */
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

    /**
     * The method returns a Boolean value, based on whether the entered product quantity is available
     * @param titolo
     * @param quantita
     * @return if the product is Availability
     */
    @Override
    public boolean checkAvailability(String titolo, Long quantita) {
        Optional<Prodotto> prodotto = prodottoRepository.findByTitolo(titolo);
        Long quantitaDisponibile = Long.valueOf(prodotto.get().getQuantita());
        if(quantitaDisponibile>=quantita && prodotto.get().getDeleted()==false){
            return true;
        }
        return false;
    }

    /**
     * The method updates the quantity of the product
     * @param titolo
     * @param quantita
     */
    @Override
    public void updateQuantity(String titolo, Long quantita) {
        if(!checkAvailability(titolo,quantita)){
            throw new CustomException("The quantity of the product is not sufficient", HttpStatus.CONFLICT);
        }
        Optional<Prodotto> prodotto = prodottoRepository.findByTitolo(titolo);
        Long quantitaDisponibile = Long.valueOf(prodotto.get().getQuantita());
        String newQuantita = String.valueOf(quantitaDisponibile-quantita);
        prodotto.get().setQuantita(newQuantita);
        //prodottoRepository.deleteByTitolo(titolo);
        prodottoRepository.save(prodotto.get());
    }

    /**
     * The method updates the quantity of the product and the price of the product associated with the product title
     * @param titolo
     * @param prezzo
     * @param quantita
     */
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

