package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Ordine;
import it.elis.sicilianaturalmente.model.Prodotto;
import org.springframework.http.HttpStatus;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AccountService {

    public Account getAccountByEmail(String email);
    public String signup(Account account);
    public String signin(String email,String password);
    public void deleteAccount(String email);
    public Account search(Long id);
    public Account whoami(HttpServletRequest request);
    public List<Account> getAccounts();
    public void changeRole(String email);
    public void addOnFavoriteList(String titolo);
    public void deleteProductFromFavoriteList(String titolo);
    public Account changeAddressInformation(String indirizzo);
    public void addOnOrderList(Ordine ordine);
    public void passwordRecovery(String email) throws MessagingException;
    public void changePassword(String email, String password);


}
