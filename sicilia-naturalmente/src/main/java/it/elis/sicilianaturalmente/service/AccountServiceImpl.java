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

    @Override
    public Account getAccount(String nome) {
        return accountRepository.findByNome(nome).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public String signup(Account account) {
        if (!accountRepository.existsAccountByEmail(account.getEmail())) {
            if(validate(account.getEmail())){
                if(account.getPassword().length()>5){
                    account.setPassword(passwordEncoder.encode(account.getPassword()));
                    accountRepository.save(account);
                    return jwtTokenProvider.createToken(account.getEmail(), Collections.singletonList(account.getRuolo()));
                }else{
                    throw new CustomException("The password must contain at least 6 characters", HttpStatus.BAD_REQUEST);
                }
            }else{
                throw new CustomException("The email you entered is not valid", HttpStatus.BAD_REQUEST);
            }
          } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

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

    @Override
    public String signin(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return jwtTokenProvider.createToken(email, Collections.singletonList(accountRepository.findByEmail(email).get().getRuolo()));
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

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


    @Override
    public Account search(Long id) {
        if (accountRepository.findById(id).isEmpty()) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        Account account = accountRepository.findById(id).get();
        return account;
    }

    @Override
    public Account whoami(HttpServletRequest req) {
        return accountRepository.findByEmail(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req))).get();
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        if(accounts==null){
            throw new CustomException("There are no users", HttpStatus.NOT_FOUND);
        }
        return accounts;
    }

    @Override
    public void addOnFavoriteList(Prodotto prodotto) {
        Optional<Prodotto> newProdotto = prodottoRepository.findByTitolo(prodotto.getTitolo());
        if(newProdotto.isEmpty() || prodotto.getDeleted()){
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

    @Override
    public void deleteProductFromFavoriteList(Prodotto prodotto) {
        Optional<Prodotto> newProdotto = prodottoRepository.findByTitolo(prodotto.getTitolo());
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

    @Override
    public Account changeAddressInformation(Account account) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Account newAccount = whoami(request);
        newAccount.setIndirizzo(account.getIndirizzo());
        //accountRepository.deleteByEmail(newAccount.getEmail());
        accountRepository.save(newAccount);
        return newAccount;
    }

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

    @Override
    public void passwordRecovery(Account account) throws MessagingException {
        if(accountRepository.existsAccountByEmail(account.getEmail())){
            String generatedPassword = randomString(8);
            String newPassword = passwordEncoder.encode(generatedPassword);
            String subject = "Gentile utente,\n" +
                    "\n" +
                    "Di seguito la sua nuova password:\n" +
                    "\n" + generatedPassword;
                    emailService.sendMail(account.getEmail(),"Recupero password",subject);
            changePassword(account,newPassword);
        }else{
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void changePassword(Account account, String password) {
        if(password.length()>5){
            Account newAccount = new Account();
            if(account == null){
                password = passwordEncoder.encode(password);
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
                newAccount = whoami(request);
            }else{
                newAccount = accountRepository.findByEmail(account.getEmail()).get();
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

    String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
