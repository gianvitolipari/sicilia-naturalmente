package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Ordine;
import it.elis.sicilianaturalmente.model.Prodotto;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AccountService {

    public Account getAccount(String nome);
    public Account getAccountByEmail(String email);
    public String signup(Account account);
    public String signin(String email,String password);
    public void deleteAccount(String email);
    public Account search(Long id);
    public Account whoami(HttpServletRequest request);
    public List<Account> getAccounts();
    public void changeRole(String email);
    public void addOnFavoriteList(Prodotto prodotto);
    public void deleteProductFromFavoriteList(Prodotto prodotto);
    public Account changeAddressInformation(Account account);
    public void addOnOrderList(Ordine ordine);


}
