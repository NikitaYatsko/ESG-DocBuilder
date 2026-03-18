package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceItemRepository extends JpaRepository <InvoiceItem, Long> {
    void deleteByInvoiceId(Long invoiceId);
}
