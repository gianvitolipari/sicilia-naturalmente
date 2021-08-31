package it.elis.sicilianaturalmente.controller;

import it.elis.sicilianaturalmente.model.Formato;
import it.elis.sicilianaturalmente.model.Prodotto;
import it.elis.sicilianaturalmente.model.RegexData;
import it.elis.sicilianaturalmente.model.Ruolo;
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

    //API used in order to return the entire list of products
    // available within the e-commerce

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/")
    public ResponseEntity<List<Prodotto>> getProducts() {
        return ResponseEntity.ok(prodottoService.getAllProduct());
    }

    //API utilizzata al fine di restituire il singolo prodotto,
    // effettuando una ricerca per il campo obbligatorio titolo
    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/{id}")
    public ResponseEntity<Prodotto> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(prodottoService.getProductByTitolo(id));
    }

    //API used in order to create a product, the parameter
    // passed in @RequestBody is a Product object
    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createProduct(@RequestBody Prodotto prodotto) {
        RegexData regexData = prodotto.validateProduct();
        if(regexData.isValid()){
            prodottoService.createProduct(prodotto);
            return ResponseEntity.ok("Product creation successful");
        }else{
            return ResponseEntity.badRequest().body(regexData.getError());
        }
    }

    //API used in order to delete a product, the search for
    // the product to be deleted is carried out through the unique title field
    @CrossOrigin(origins = {"http://localhost:3000"})
    @DeleteMapping("/delete/{titolo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable String titolo) {
        prodottoService.deleteProduct(titolo);
        return ResponseEntity.ok("Product deleted successfully");
    }

    //API used to return a list of products searched through the format field
    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/format/{formato}")
    public ResponseEntity<List<Prodotto>> getFormato(@PathVariable Formato formato) {
        return ResponseEntity.ok(prodottoService.getFormato(formato));
    }

    //API used to return a list of products sorted in ascending order referring to the price
    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/price")
    public ResponseEntity<List<Prodotto>> orderByPrice() {
        return ResponseEntity.ok(prodottoService.orderByPrice());
    }

    //API used in order to return a list of products searched through a regex
    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/research/{titolo}")
    public ResponseEntity<List<Prodotto>> getProductByRegex(@PathVariable String titolo) {
        return ResponseEntity.ok(prodottoService.getByRegex(titolo));
    }

    //API used in order to make changes to the product, in particular
    // to modify the "price" and "quantity" fields, the search for the product
    // to be modified is carried out through the unique title field
    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/editProduct")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> changeProduct(@RequestParam("titolo") String titolo,@RequestParam("prezzo") Float prezzo, @RequestParam("quantita") String quantita) {
        Prodotto prodotto = new Prodotto().setQuantita(quantita)
                .setPrezzo(prezzo)
                .setTitolo(titolo);
        RegexData regexData = prodotto.validateProduct();
        if(regexData.isValid()){
            prodottoService.changeProduct(titolo,prezzo,quantita);
            return ResponseEntity.ok("Correctly modified product");
        }else{
            return ResponseEntity.badRequest().body(regexData.getError());
        }
    }

}
