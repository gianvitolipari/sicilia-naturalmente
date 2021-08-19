package it.elis.sicilianaturalmente.controller;


import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Ordine;
import it.elis.sicilianaturalmente.model.Prodotto;
import it.elis.sicilianaturalmente.service.AccountService;
import it.elis.sicilianaturalmente.service.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public ResponseEntity<String> delete(@RequestBody Account account) {
        accountService.deleteAccount(account.getEmail());
        return ResponseEntity.ok("Cancellazione dell'Account " + account.getEmail().toString() + " effettuata correttamente");
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Account> search(@PathVariable String id) {
        return ResponseEntity.ok(accountService.search(Long.valueOf(id).longValue()));
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping(value = "/accounts")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
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
    public ResponseEntity<String> changeRole(@RequestBody Account account) {
        accountService.changeRole(account.getEmail());
        return ResponseEntity.ok("Permission changed successfully");
    }


    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/favorite")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> addOnFavoriteList(@RequestBody Prodotto prodotto) {
        accountService.addOnFavoriteList(prodotto);
        return ResponseEntity.ok("Product inserted correctly in the list of favorites");
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @DeleteMapping("/favorite/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> deleteProductFromFavoriteList(@RequestBody Prodotto prodotto) {
        accountService.deleteProductFromFavoriteList(prodotto);
        return ResponseEntity.ok("The product has been successfully removed from the favorites list");
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/address")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<Account> changeAddressInformation(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.changeAddressInformation(account));
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/order")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> addOnFavoriteList(@RequestBody Ordine ordine) {
        accountService.addOnOrderList(ordine);
        return ResponseEntity.ok("Order inserted correctly in the list of favorites");
    }
}

