package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.request.NewBankCategoryRequest;
import esg.esgdocbuilder.model.dto.response.BankCategoryResponse;
import esg.esgdocbuilder.service.impl.BankingCategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bank-categories")
@RequiredArgsConstructor
public class BankCategoriesController {
    private final BankingCategoryServiceImpl bankingCategoryService;

    @GetMapping
    public ResponseEntity<List<BankCategoryResponse>> getBankCategories() {
        return ResponseEntity.ok(bankingCategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankCategoryResponse> getBankCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(bankingCategoryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<BankCategoryResponse> createNewBankCategory(@RequestBody NewBankCategoryRequest request) {
        return ResponseEntity.ok(bankingCategoryService.create(request));
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<BankCategoryResponse> updateBankCategory(@RequestBody NewBankCategoryRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(bankingCategoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public void deleteBankCategoryById(@PathVariable Long id) {
        bankingCategoryService.delete(id);
    }

}
