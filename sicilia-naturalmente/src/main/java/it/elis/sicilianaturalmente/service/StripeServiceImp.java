package it.elis.sicilianaturalmente.service;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.PaymentData;
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

   /* @Autowired
    StripeServiceImp() {
        Stripe.apiKey = "sk_test_uTAM1qndRDbiJRowe8dJf6x9";
    }*/

    public Customer createCustomer(String token, String email) throws Exception {
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("email", email);
        customerParams.put("source", token);
        return Customer.create(customerParams);
    }

    private Customer getCustomer(String id) throws Exception {
        return Customer.retrieve(id);
    }

    @Override
    public Customer createCustomer() throws Exception {
        Stripe.apiKey = stripeApiKey;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Account account = accountService.whoami(request);

        Map<String, Object> params = new HashMap<>();
        params.put("email",account.getEmail());
        params.put("name",account.getNome());

        Customer stripeCustomer = Customer.create(params);
        return stripeCustomer;
    }

    public Charge chargeNewCard(String token, double amount) throws Exception {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int)(amount * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        return charge;
    }

    public Charge chargeCustomerCard(String customerId, int amount) throws Exception {
        String sourceCard = getCustomer(customerId).getDefaultSource();
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", "USD");
        chargeParams.put("customer", customerId);
        chargeParams.put("source", sourceCard);
        Charge charge = Charge.create(chargeParams);
        return charge;
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
}