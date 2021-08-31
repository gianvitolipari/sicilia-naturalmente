package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.exception.CustomException;
import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Ordine;
import it.elis.sicilianaturalmente.model.Prodotto;
import it.elis.sicilianaturalmente.model.Ruolo;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import it.elis.sicilianaturalmente.repository.ProdottoRepository;
import it.elis.sicilianaturalmente.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProdottoRepository prodottoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    /**
     * The method returns an Account by searching by user email
     * @param email
     * @return Account
     */
    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());
    }

    /**
     * The method makes a registration within the system
     * @param account
     * @return String token
     */
    @Override
    public String signup(Account account) {
        if(account.getNome()==null || account.getPassword()==null || account.getEmail()==null){
            throw new CustomException("Check that you have entered all the required fields", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!accountRepository.existsAccountByEmail(account.getEmail())) {
                        account.setPassword(passwordEncoder.encode(account.getPassword()));
                        accountRepository.save(account);
                        return jwtTokenProvider.createToken(account.getEmail(), Collections.singletonList(account.getRuolo()));
          } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * The method changes the role of a particular user searched for by email
     * @param email
     */
    @Override
    public void changeRole(String email) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isEmpty()) {
            throw new CustomException("There is no user with this email", HttpStatus.NOT_FOUND);
        }
        Account account = optionalAccount.get();
        if (account.getRuolo().equals(Ruolo.ROLE_ADMIN)) {
            Integer numeroAdmin = accountRepository.countByRuolo(Ruolo.ROLE_ADMIN);
            if (numeroAdmin == 1) {
                throw new CustomException("You cannot change your role, otherwise there would be no more admins", HttpStatus.UNPROCESSABLE_ENTITY);
            } else {
                account.setRuolo(Ruolo.ROLE_CLIENT);
            }
        } else {
            account.setRuolo(Ruolo.ROLE_ADMIN);
        }
        //accountRepository.deleteByEmail(email);
        accountRepository.save(account);
    }

    /**
     * The method logs in through the email and password fields
     * @param email
     * @param password
     * @return String token
     */
    @Override
    public String signin(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return jwtTokenProvider.createToken(email, Collections.singletonList(accountRepository.findByEmail(email).get().getRuolo()));
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * The method deletes a particular account through the email field
     * @param email
     */
    @Override
    public void deleteAccount(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if(account.isEmpty()){
            throw new CustomException("there is no user with this id", HttpStatus.NOT_FOUND);
        }
        if(account.get().getRuolo().equals(Ruolo.ROLE_ADMIN)){
           Integer numeroAdmin = accountRepository.countByRuolo(Ruolo.ROLE_ADMIN);
           if(numeroAdmin==1){
               throw new CustomException("You cannot delete this user, it is the last Admin left", HttpStatus.UNPROCESSABLE_ENTITY);
           }
        }
        accountRepository.deleteByEmail(email);
    }


    /**
     * The method searches for a particular user through the email field
     * @param id
     * @return Account
     */
    @Override
    public Account search(Long id) {
        if(id==null){
            throw new CustomException("Insert a valid value for idAccount field", HttpStatus.BAD_REQUEST);
        }
        if (accountRepository.findById(id).isEmpty()) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        Account account = accountRepository.findById(id).get();
        return account;
    }

    /**
     * The method returns all the data relating to the account making the request
     * @param req
     * @return Account
     */
    @Override
    public Account whoami(HttpServletRequest req) {
        return accountRepository.findByEmail(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req))).get();
    }

    /**
     * The method returns the list of all users registered in the system
     * @return List<Account>
     */
    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        if(accounts==null){
            throw new CustomException("There are no users", HttpStatus.NOT_FOUND);
        }
        return accounts;
    }

    /**
     * The method adds a product to the favorite list associated with the user making the request
     * @param titolo
     */
    @Override
    public void addOnFavoriteList(String titolo) {
        Optional<Prodotto> newProdotto = prodottoRepository.findByTitolo(titolo);
        if(newProdotto.isEmpty() || newProdotto.get().getDeleted()){
            throw new CustomException("Product not found", HttpStatus.NOT_FOUND);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Account account = whoami(request);
        if(account.getFavoriti().contains(newProdotto.get())){
            throw new CustomException("The product already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }else{
            account.getFavoriti().add(newProdotto.get());
            //accountRepository.deleteByEmail(account.getEmail());
            accountRepository.save(account);
        }

    }

    /**
     * The method removes a product from the favorites list associated with the user making the request
     * @param titolo
     */
    @Override
    public void deleteProductFromFavoriteList(String titolo) {
        Optional<Prodotto> newProdotto = prodottoRepository.findByTitolo(titolo);
        if(newProdotto.isEmpty() || newProdotto.get().getDeleted()){
            throw new CustomException("Product not found", HttpStatus.NOT_FOUND);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Account account = whoami(request);
        if(!account.getFavoriti().contains(newProdotto.get())){
            throw new CustomException("The product is not present in the favorites list", HttpStatus.UNPROCESSABLE_ENTITY);
        }else{
            account.getFavoriti().remove(newProdotto.get());
            //accountRepository.deleteByEmail(account.getEmail());
            accountRepository.save(account);
        }

    }

    /**
     * The method changes a shipping address of the user making the request
     * @param indirizzo
     * @return Account
     */
    @Override
    public Account changeAddressInformation(String indirizzo) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Account newAccount = whoami(request);
        newAccount.setIndirizzo(indirizzo);
        //accountRepository.deleteByEmail(newAccount.getEmail());
        accountRepository.save(newAccount);
        return newAccount;
    }

    /**
     * The method enters the order in the user's order list
     * @param ordine
     */
    @Override
    public void addOnOrderList(Ordine ordine) {
        if(ordine == null){
            throw new CustomException("Please enter a valid order", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Account account = whoami(request);
        account.getOrdini().add(ordine);
        accountRepository.save(account);
    }


    /**
     * The method sends an email to perform the password recovery
     * and generates a new password which will be nothing but the new one
     * @param email
     * @throws MessagingException
     */
    @Override
    public void passwordRecovery(String email) throws MessagingException {
        if(accountRepository.existsAccountByEmail(email)){
            String generatedPassword = randomString(8);
            String newPassword = passwordEncoder.encode(generatedPassword);
            String subject = "Gentile utente,\n" +
                    "\n" +
                    "Di seguito la sua nuova password:\n" +
                    "\n" + generatedPassword;
                    emailService.sendMail(email,"Recupero password",subject);
            changePassword(email,newPassword);
        }else{
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * The method changes the password to the user currently logged in
     * @param email
     * @param password
     */
    @Override
    public void changePassword(String email, String password) {
        if(password.length()>5){
            Account newAccount = new Account();
            if(email == null){
                password = passwordEncoder.encode(password);
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
                newAccount = whoami(request);
            }else{
                newAccount = accountRepository.findByEmail(email).get();
            }
            newAccount.setPassword(password);
            //accountRepository.deleteByEmail(newAccount.getEmail());
            accountRepository.save(newAccount);
        }else{
            throw new CustomException("The password must contain at least 6 characters", HttpStatus.BAD_REQUEST);
        }
    }


    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    /**
     * The method generates a new string
     * @param len
     * @return String random password
     */
    String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }


}
