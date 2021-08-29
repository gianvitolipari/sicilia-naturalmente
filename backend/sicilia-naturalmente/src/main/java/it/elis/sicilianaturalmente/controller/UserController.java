package it.elis.sicilianaturalmente.controller;


import it.elis.sicilianaturalmente.exception.CustomException;
import it.elis.sicilianaturalmente.model.*;
import it.elis.sicilianaturalmente.service.AccountService;
import it.elis.sicilianaturalmente.service.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static it.elis.sicilianaturalmente.model.Account.validateAddress;
import static it.elis.sicilianaturalmente.model.Account.validatePassword;
import static it.elis.sicilianaturalmente.model.Email.validateEmail;
import static it.elis.sicilianaturalmente.model.Ordine.validateIdOrdine;
import static it.elis.sicilianaturalmente.model.Prodotto.validateProductTitolo;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    OrdineService ordineService;
    @Autowired
    AccountService accountService;

    @CrossOrigin(origins = {"http://localhost:3000"})
    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> delete(@RequestParam("email") String email) {
        RegexData regexData = validateEmail(email);
        if(regexData.isValid())
        {
            accountService.deleteAccount(email);
            return ResponseEntity.ok("Cancellazione dell'Account " + email + " effettuata correttamente");
        }else{
            return ResponseEntity.badRequest().body(regexData.getError());
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Account> search(@PathVariable String id) {
        return ResponseEntity.ok(accountService.search(Long.valueOf(id).longValue()));
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping(value = "/accounts")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok(accountService.getAccounts());
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<Account> whoami(HttpServletRequest req) {
        return ResponseEntity.ok(accountService.whoami(req));
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> changeRole(@RequestParam("email") String email) {
        RegexData regexData = validateEmail(email);
        if(regexData.isValid())
        {
            accountService.changeRole(email);
            return ResponseEntity.ok("Permission changed successfully");
           }else{
            return ResponseEntity.badRequest().body(regexData.getError());
        }
    }


    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/favorite")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> addOnFavoriteList(@RequestParam("titolo") String titolo) {
        RegexData regexData = validateProductTitolo(titolo);
        if(regexData.isValid())
        {
            accountService.addOnFavoriteList(titolo);
            return ResponseEntity.ok("Product inserted correctly in the list of favorites");
        }else{
            return ResponseEntity.badRequest().body(regexData.getError());
        }

    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @DeleteMapping("/favorite/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> deleteProductFromFavoriteList(@RequestParam("titolo") String titolo) {
        RegexData regexData = validateProductTitolo(titolo);
        if(regexData.isValid())
        {
            accountService.deleteProductFromFavoriteList(titolo);
            return ResponseEntity.ok("The product has been successfully removed from the favorites list");
        }else{
            return ResponseEntity.badRequest().body(regexData.getError());
        }

    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/address")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> changeAddressInformation(@RequestParam("indirizzo") String indirizzo) {
        RegexData regexData = validateAddress(indirizzo);
        if(regexData.isValid())
        {
            accountService.changeAddressInformation(indirizzo);
            return ResponseEntity.ok("The address information has been successfully changed");
        }else{
            return ResponseEntity.badRequest().body(regexData.getError());
        }
    }

    /*
    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/order")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> addOnFavoriteList(@RequestBody Ordine ordine) {
        accountService.addOnOrderList(ordine);
        return ResponseEntity.ok("Order inserted correctly in the list of favorites");
    }

     */

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> changePassword(@RequestParam("password") String password) {
        RegexData regexData = validatePassword(password);
        if(regexData.isValid())
        {
            accountService.changePassword(null,password);
            return ResponseEntity.ok("The password was changed correctly");
        }else{
            return ResponseEntity.badRequest().body(regexData.getError());
        }

    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/order/content/{idOrdine}")
    public ResponseEntity<List<ContenutoProdotto>> getContenutoOrdine(@PathVariable Long idOrdine) {
        return ResponseEntity.ok(ordineService.getContenutoOrdine(idOrdine));
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/order/account")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Ordine>> getOrders(@RequestParam("email") String email) {
        RegexData regexData = validateEmail(email);
        if(regexData.isValid())
        {
            return ResponseEntity.ok(ordineService.getOrders(email));
        }else{
            throw new CustomException(regexData.getError(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/order/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> changeStatus(@RequestParam("idOrdine") Long idOrdine, @RequestParam("statoPagamento") Stato statoPagamento) {
        RegexData regexData = validateIdOrdine(idOrdine);
        if(regexData.isValid())
        {
            ordineService.changeStatus(idOrdine,statoPagamento);
            return ResponseEntity.ok("The order status was successfully changed");
        }else{
            throw new CustomException(regexData.getError(), HttpStatus.NOT_FOUND);
        }
    }
}

