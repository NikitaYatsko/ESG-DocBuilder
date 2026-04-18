package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.Product;
import esg.esgdocbuilder.model.enums.TypeOfUnitEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = "category")
    Page<Product> findAll(Pageable pageable);


    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);


    Page<Product> findByTypeOfUnit(TypeOfUnitEnum UnitId, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    @Query("""
            select p
            from Product p
            join p.category c 
            where lower(p.name) like lower(concat('%', :query, '%'))
               or lower(c.name) like lower(concat('%', :query, '%'))
            """)
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);
}
