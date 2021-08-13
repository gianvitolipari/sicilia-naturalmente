package it.elis.sicilianaturalmente.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeController {

    @GetMapping("/stripe")
    public String result() {
        // return accountService.getAccount("");
        return "stripe";
    }
}
