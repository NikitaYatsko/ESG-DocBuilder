package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.Invoice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository <Invoice, Long> {
    @Override
    @EntityGraph(attributePaths = "invoiceItems")
    List<Invoice> findAll();

    @Override
    @EntityGraph(attributePaths = "invoiceItems")
    Optional<Invoice> findById(Long id);
}
