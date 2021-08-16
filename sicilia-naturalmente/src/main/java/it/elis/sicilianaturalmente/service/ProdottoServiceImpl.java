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
        if(prodotto.isEmpty()){
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
        return prodottoRepository.findAll();
    }

    @Override
    public void createProduct(Prodotto prodotto) {
        Optional<Prodotto> newProdotto = prodottoRepository.findByTitolo(prodotto.getTitolo());
        if(!newProdotto.isEmpty()){
            throw new CustomException("The product already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        prodottoRepository.save(prodotto);
    }

    @Override
    public void deleteProduct(String titolo) {
        Optional<Prodotto> prodotto = prodottoRepository.findByTitolo(titolo);
        if(prodotto.isEmpty()){
            throw new CustomException("There is no product with this idProduct", HttpStatus.NOT_FOUND);
        }
        prodottoRepository.deleteByTitolo(titolo);
    }

    @Override
    public List<Prodotto> getFormato(Formato formato) {

        return prodottoRepository.findAllByFormato(formato);
    }

    @Override
    public List<Prodotto> orderByPrice() {
        if(prodottoRepository.findAll().isEmpty()){
            throw new CustomException("There is no products", HttpStatus.NOT_FOUND);
        }
        return prodottoRepository.findByOrderByPrezzoAsc();
    }

    @Override
    public List<Prodotto> getByRegex(String titolo) {
        if(titolo != null){
            List<Prodotto> productsList= prodottoRepository.findByTitoloOrderByTitoloAsc(titolo);
            return productsList;
        }
        throw new CustomException("There is no products", HttpStatus.NOT_FOUND);
    }

}
