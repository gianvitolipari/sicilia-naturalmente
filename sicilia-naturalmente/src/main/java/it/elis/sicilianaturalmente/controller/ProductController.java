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

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/")
    public ResponseEntity<List<Prodotto>> getProducts() {
        return ResponseEntity.ok(prodottoService.getAllProduct());
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/{id}")
    public ResponseEntity<Prodotto> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(prodottoService.getProductByTitolo(id));
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createProduct(@RequestBody Prodotto prodotto) {
        prodottoService.createProduct(prodotto);
        return ResponseEntity.ok("Product creation successful");
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @DeleteMapping("/delete/{titolo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable String titolo) {
        prodottoService.deleteProduct(titolo);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/format/{formato}")
    public ResponseEntity<List<Prodotto>> getFormato(@PathVariable Formato formato) {
        return ResponseEntity.ok(prodottoService.getFormato(formato));
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/price")
    public ResponseEntity<List<Prodotto>> orderByPrice() {
        return ResponseEntity.ok(prodottoService.orderByPrice());
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/research/{titolo}")
    public ResponseEntity<List<Prodotto>> getProductByRegex(@PathVariable String titolo) {
        return ResponseEntity.ok(prodottoService.getByRegex(titolo));
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/editProduct")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> changeProduct(@RequestBody Prodotto prodotto) {
        prodottoService.changeProduct(prodotto);
        return ResponseEntity.ok("Correctly modified product");
    }

}
