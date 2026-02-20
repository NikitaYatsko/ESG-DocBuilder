package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
