package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account,Long> {
    Optional<Account> findByName(String name);

}
