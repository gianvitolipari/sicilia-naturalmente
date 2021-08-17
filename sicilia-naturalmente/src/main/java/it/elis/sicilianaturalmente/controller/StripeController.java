package it.elis.sicilianaturalmente.controller;

import com.stripe.model.Charge;
import com.stripe.model.Customer;
import it.elis.sicilianaturalmente.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/customer/create")
    public ResponseEntity<Customer> createCustomer(Customer customer) throws Exception {
        return ResponseEntity.ok(stripeService.createCustomer(customer));
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/charge")
    public Charge chargeCard(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Double amount = Double.parseDouble(request.getHeader("amount"));
        return this.stripeService.chargeNewCard(token, amount);
    }
}
