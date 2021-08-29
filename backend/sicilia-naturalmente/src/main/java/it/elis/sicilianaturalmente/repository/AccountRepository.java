package it.elis.sicilianaturalmente.repository;

import it.elis.sicilianaturalmente.model.Account;
import it.elis.sicilianaturalmente.model.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
Optional<Account> findByNome(String nome);
Optional<Account> findByEmail(String email);
void deleteAccountByEmail(String email);
@Transactional
void deleteByEmail(String email);
void deleteAccountByIdAccount(Long id);
void deleteByIdAccount(Long id);
Optional<Account> findAccountByIdAccount(Long id);
boolean existsAccountByEmail(String email);
Integer countByRuolo(Ruolo ruolo);
}
