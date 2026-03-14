package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository <Invoice, Long> {

}
