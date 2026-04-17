package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = "category")
    Page<Product> findAll(Pageable pageable);


    List<Product> findByCategoryId(Long categoryId);
}
