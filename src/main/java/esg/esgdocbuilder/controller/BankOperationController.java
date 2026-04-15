package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.AccountDTO;
import esg.esgdocbuilder.model.dto.request.BankOperationRequest;
import esg.esgdocbuilder.model.dto.response.BankOperationResponse;
import esg.esgdocbuilder.model.dto.response.PaginationResponse;
import esg.esgdocbuilder.service.BankOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<PaginationResponse<BankOperationResponse>> getAllBankOperations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size

    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bankOperationService.getAllOperations(pageable));
    }

    @PostMapping("/operations")
    public ResponseEntity<BankOperationResponse> createBankOperation(@RequestBody BankOperationRequest bankOperationRequest) {
        return ResponseEntity.ok(bankOperationService.createOperation(bankOperationRequest));
    }

    @DeleteMapping("/operations/{id}")
    public ResponseEntity<Void> deleteBankOperation(@PathVariable Long id) {
        bankOperationService.deleteOperation(id);
        return ResponseEntity.noContent().build();
    }
}
