package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.AccountDTO;
import esg.esgdocbuilder.model.dto.request.BankOperationRequest;
import esg.esgdocbuilder.model.dto.response.BankOperationResponse;
import esg.esgdocbuilder.service.BankOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankOperationController {

    private final BankOperationService bankOperationService;

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(bankOperationService.getAllAccounts());
    }

    @GetMapping("/operations")
    public ResponseEntity<List<BankOperationResponse>> getAllBankOperations() {
        return ResponseEntity.ok(bankOperationService.getAllOperations());
    }

    @PostMapping("/operations")
    public ResponseEntity<BankOperationResponse> createBankOperation(@RequestBody BankOperationRequest bankOperationRequest) {
        return ResponseEntity.ok(bankOperationService.createOperation(bankOperationRequest));
    }
}
