package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.request.BankOperationRequest;
import esg.esgdocbuilder.model.dto.response.BankOperationResponse;
import esg.esgdocbuilder.service.BankOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankOperationController {

    private final BankOperationService bankOperationService;

    public ResponseEntity<List<BankOperationResponse>> getAllBankOperations() {
        return null;
    }

    @PostMapping("/create-operation")
    public ResponseEntity<BankOperationResponse> createBankOperation(@RequestBody BankOperationRequest bankOperationRequest) {
        BankOperationResponse response = bankOperationService.createOperation(bankOperationRequest);
        return ResponseEntity.ok(response);
    }
}
