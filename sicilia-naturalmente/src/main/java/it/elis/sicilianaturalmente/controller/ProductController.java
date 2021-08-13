package it.elis.sicilianaturalmente.controller;

import it.elis.sicilianaturalmente.model.Formato;
import it.elis.sicilianaturalmente.model.Prodotto;
import it.elis.sicilianaturalmente.service.AccountService;
import it.elis.sicilianaturalmente.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    AccountService accountService;

    @Autowired
    ProdottoService prodottoService;

    @GetMapping("/")
    public ResponseEntity<List<Prodotto>> getProducts() {
        return ResponseEntity.ok(prodottoService.getAllProduct());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prodotto> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(prodottoService.getProductByTitolo(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createProduct(@RequestBody Prodotto prodotto) {
        prodottoService.createProduct(prodotto);
        return ResponseEntity.ok("Product creation successful");
    }

    @DeleteMapping("/delete/{titolo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable String titolo) {
        prodottoService.deleteProduct(titolo);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/formato/{formato}")
    public ResponseEntity<List<Prodotto>> getFormato(@PathVariable Formato formato) {
        return ResponseEntity.ok(prodottoService.getFormato(formato));
    }

    @GetMapping("/price")
    public ResponseEntity<List<Prodotto>> orderByPrice() {
        return ResponseEntity.ok(prodottoService.orderByPrice());
    }

}
