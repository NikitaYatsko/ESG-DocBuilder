package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.NewBankCategoryRequest;
import esg.esgdocbuilder.model.dto.response.BankCategoryResponse;


import java.util.List;

public interface
BankingCategoryService {
    BankCategoryResponse create(NewBankCategoryRequest request);
    List<BankCategoryResponse> getAll();
    BankCategoryResponse getById(Long id);
    BankCategoryResponse update(Long id, NewBankCategoryRequest request);
    void delete(Long id);
}
