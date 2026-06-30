package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.AccountDTO;
import esg.esgdocbuilder.model.dto.request.BankOperationRequest;
import esg.esgdocbuilder.model.dto.response.BankOperationResponse;
import esg.esgdocbuilder.model.dto.response.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BankOperationService {
    BankOperationResponse createOperation(BankOperationRequest bankOperationRequest);

    PaginationResponse<BankOperationResponse> getAllOperations(Pageable pageable);

    PaginationResponse<BankOperationResponse> getAllOperationsFiltered(
            Pageable pageable,
            LocalDate from,
            LocalDate to
    );

    List<AccountDTO> getAllAccounts();

    void deleteOperation(Long id);

    List<BankOperationResponse> getOperationsForPeriod(LocalDate from, LocalDate to);
}
