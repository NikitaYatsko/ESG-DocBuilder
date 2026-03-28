package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.AccountDTO;
import esg.esgdocbuilder.model.dto.request.BankOperationRequest;
import esg.esgdocbuilder.model.dto.response.BankOperationResponse;
import esg.esgdocbuilder.model.dto.response.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BankOperationService {
    BankOperationResponse createOperation(BankOperationRequest bankOperationRequest);
    PaginationResponse<BankOperationResponse> getAllOperations(Pageable pageable);
    List<AccountDTO> getAllAccounts();
}
