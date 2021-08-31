package it.elis.sicilianaturalmente.service;

import com.stripe.Stripe;
import com.stripe.model.*;
import it.elis.sicilianaturalmente.exception.CustomException;
import it.elis.sicilianaturalmente.model.*;
import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class StripeServiceImp implements StripeService{

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripeApiKey;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProdottoService prodottoService;

    @Autowired
    private OrdineService ordineService;

    @Autowired
    private EmailService emailService;

    /**
     * The method creates a customer stripe
     * @return Customer
     * @throws Exception
     */
    @Override
    public Customer createCustomer() throws Exception {
        Stripe.apiKey = stripeApiKey;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Account account = accountService.whoami(request);
        Account me = accountRepository.getById(account.getIdAccount());
        if(me.getCustomer_id()==null){
            Map<String, Object> params = new HashMap<>();
            params.put("email",account.getEmail());
            params.put("name",account.getNome());

            Customer stripeCustomer = Customer.create(params);
            me.setCustomer_id(stripeCustomer.getId());
            accountRepository.deleteByEmail(me.getEmail());
            accountRepository.save(me);
            return stripeCustomer;
        }
        Customer customer = new Customer();
        customer.setId(me.getCustomer_id());
        customer.setEmail(me.getEmail());
        customer.setName(me.getNome());
        return customer;
    }


    /**
     * The method creates a payment intents
     * @param paymentData
     * @throws Exception
     */
    @Override
    public void createPaymentIntent(PaymentData paymentData) throws Exception {

        if(paymentData.getProducts()==null || paymentData.getPrice()==null){
            throw new CustomException("Check that you have entered all the required fields", HttpStatus.BAD_REQUEST);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Account account = accountService.whoami(request);

        Stripe.apiKey = stripeApiKey;
        List<Object> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentData.getPrice());
        params.put("payment_method", paymentData.getPaymentMethod());
        params.put("currency", "eur");
        String customerId = createCustomer().getId();
        params.put("customer", customerId);
        params.put("confirm", true);
        params.put("payment_method_types", paymentMethodTypes );

        boolean quantitaOK = true;
        for (Prodotto p: paymentData.getProducts()) {
            if(!prodottoService.checkAvailability(p.getTitolo(),Long.valueOf(p.getQuantita()))){
                quantitaOK=false;
            }
        }
        if(!quantitaOK){
            throw new CustomException("One of the products inserted is no longer available", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Ordine ordine = ordineService.createOrder(paymentData);
        if(paymentData.getPaymentMethod()!=null){
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            ordine.setStatoPagamento(StatoPagamento.PAGATO);

        }


        accountService.addOnOrderList(ordine);
        for (Prodotto p:paymentData.getProducts()) {
            prodottoService.updateQuantity(p.getTitolo(),Long.valueOf(p.getQuantita()));
        }
        String subject = ordine.toString();

        emailService.sendMail(account.getEmail(),"Riepilogo ordine effettuato",subject);


    }


    /**
     * The method creates a payment method
     * @param newPaymentMethod
     * @return PaymentMethod
     * @throws Exception
     */
    @Override
    public PaymentMethod addPaymentMethod(String newPaymentMethod) throws Exception {
        if(newPaymentMethod==null){
            throw new CustomException("Please enter a valid payment method", HttpStatus.BAD_REQUEST);
        }
        Stripe.apiKey = stripeApiKey;
        PaymentMethod paymentMethod =
                PaymentMethod.retrieve(newPaymentMethod);

        Map<String, Object> params = new HashMap<>();
        params.put("customer", createCustomer().getId());

        PaymentMethod updatedPaymentMethod =
                paymentMethod.attach(params);
        return updatedPaymentMethod;
    }

    /**
     * The method returns the data associated with the card for the user making the request
     * @return Customer
     * @throws Exception
     */
    @Override
    public Customer getCustomer() throws Exception {
        Stripe.apiKey = stripeApiKey;

        Customer customer =
                Customer.retrieve(createCustomer().getId());
        return customer;
    }

    /**
     * The method returns the list of payment methods associated with a particular user
     * @return List<PaymentMethodData>
     * @throws Exception
     */
    @Override
    public List<PaymentMethodData> getPaymentMethod() throws Exception {
        Stripe.apiKey = stripeApiKey;
        Map<String, Object> params = new HashMap<>();
        params.put("customer", createCustomer().getId());
        params.put("type", "card");

        PaymentMethodCollection paymentMethods =
                PaymentMethod.list(params);
        List<PaymentMethodData> paymentData = new ArrayList<>();
        for (PaymentMethod paymentMethod : paymentMethods.getData()) {
            paymentData.add(new PaymentMethodData().setPaymentMethodId(paymentMethod.getId()).setLast4(paymentMethod.getCard().getLast4()));
        }
        return paymentData;
    }
}