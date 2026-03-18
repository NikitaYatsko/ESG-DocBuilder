package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.AccountDTO;
import esg.esgdocbuilder.model.dto.request.BankOperationRequest;
import esg.esgdocbuilder.model.dto.response.BankOperationResponse;

import java.util.List;

public interface BankOperationService {
    BankOperationResponse createOperation(BankOperationRequest bankOperationRequest);
    List<BankOperationResponse> getAllOperations();
    List<AccountDTO> getAllAccounts();
}
