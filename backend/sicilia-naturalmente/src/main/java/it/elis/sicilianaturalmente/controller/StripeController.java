package it.elis.sicilianaturalmente.controller;

import com.stripe.model.*;
import it.elis.sicilianaturalmente.model.PaymentData;
import it.elis.sicilianaturalmente.model.PaymentMethodData;
import it.elis.sicilianaturalmente.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class StripeController {

    private StripeService stripeService;

    @Autowired
    StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    //API used to receive the custumer_id useful in order to associate a payment card
    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/customer_id")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> getCustomerId() throws Exception {
        Customer stripeCustomer = stripeService.createCustomer();
        return ResponseEntity.ok(stripeCustomer.getId());
    }

    //API used to create a payment intents, the front-end will have to pass a payment
    // method, the checkout price and the list of products to be purchased with the
    // additional quantity of each
    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/payment_intents")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentData paymentData) throws Exception {
        stripeService.createPaymentIntent(paymentData);
        return ResponseEntity.ok("Payment successful");
    }

    //API used to add a payment method to a particular user
    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/customer")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> addPaymentMethod(@RequestBody String payment_method) throws Exception {
        PaymentMethod newPaymentMethod = stripeService.addPaymentMethod(payment_method);
        return ResponseEntity.ok("Il metodo di pagamento con id:"+newPaymentMethod.getId()+" e' stato aggiunto correttamente");
    }

    //API used to receive all information relating to the payment data of the user making the request
    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/customer")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> getCustomer() throws Exception {
        Customer stripeCustomer = stripeService.getCustomer();
        return ResponseEntity.ok(stripeCustomer.toString());
    }

    //API used to receive all payment methods associated with the user making the request
    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/customer_payment_methods")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<PaymentMethodData>> getPaymentMethodCollection() throws Exception {
        List<PaymentMethodData> paymentMethodCollection = stripeService.getPaymentMethod();
        return ResponseEntity.ok(paymentMethodCollection);
    }
}
