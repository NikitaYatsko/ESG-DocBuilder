package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    @EntityGraph(attributePaths = "category")
    Page<Product> findAll(Pageable pageable);
}
