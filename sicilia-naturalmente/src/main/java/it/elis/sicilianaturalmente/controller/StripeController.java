package it.elis.sicilianaturalmente.controller;

import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import it.elis.sicilianaturalmente.model.PaymentData;
import it.elis.sicilianaturalmente.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/payment")
public class StripeController {

    private StripeService stripeService;

    @Autowired
    StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/customer/create")
    public ResponseEntity<String> createCustomer() throws Exception {
        Customer stripeCustomer = stripeService.createCustomer();
        return ResponseEntity.ok(stripeCustomer.getId());
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/payment_intents")
    public ResponseEntity<String> createCustomer(@RequestBody PaymentData paymentData) throws Exception {
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(paymentData);
        return ResponseEntity.ok(paymentIntent.getId());
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/charge")
    public Charge chargeCard(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Double amount = Double.parseDouble(request.getHeader("amount"));
        return this.stripeService.chargeNewCard(token, amount);
    }
}
