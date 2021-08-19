package it.elis.sicilianaturalmente.service;

import com.stripe.Stripe;
import com.stripe.model.*;
import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.PaymentData;
import it.elis.sicilianaturalmente.model.PaymentMethodData;
import it.elis.sicilianaturalmente.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StripeServiceImp implements StripeService{

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripeApiKey;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

   /* @Autowired
    StripeServiceImp() {
        Stripe.apiKey = "sk_test_uTAM1qndRDbiJRowe8dJf6x9";
    }*/

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



    @Override
    public PaymentIntent createPaymentIntent(PaymentData paymentData) throws Exception {
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

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent;
    }

    @Override
    public PaymentMethod addPaymentMethod(String newPaymentMethod) throws Exception {
        Stripe.apiKey = stripeApiKey;
        PaymentMethod paymentMethod =
                PaymentMethod.retrieve(newPaymentMethod);

        Map<String, Object> params = new HashMap<>();
        params.put("customer", createCustomer().getId());

        PaymentMethod updatedPaymentMethod =
                paymentMethod.attach(params);
        return updatedPaymentMethod;
    }

    @Override
    public Customer getCustomer() throws Exception {
        Stripe.apiKey = stripeApiKey;

        Customer customer =
                Customer.retrieve(createCustomer().getId());
        return customer;
    }

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