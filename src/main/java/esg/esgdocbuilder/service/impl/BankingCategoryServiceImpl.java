package esg.esgdocbuilder.service.impl;


import esg.esgdocbuilder.mapper.BankOperationMapper;
import esg.esgdocbuilder.model.dto.request.NewBankCategoryRequest;
import esg.esgdocbuilder.model.dto.response.BankCategoryResponse;
import esg.esgdocbuilder.model.entity.BankingCategory;
import esg.esgdocbuilder.repository.BankingCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankingCategoryServiceImpl {

    private final BankingCategoryRepository repository;
    private final BankOperationMapper mapper;

    public BankCategoryResponse create(NewBankCategoryRequest request) {
        BankingCategory category = mapper.toEntity(request);
        repository.save(category);
        return mapper.toResponse(category);
    }

    public List<BankCategoryResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public BankCategoryResponse getById(Long id) {
        BankingCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return mapper.toResponse(category);
    }

    public BankCategoryResponse update(Long id, NewBankCategoryRequest request) {
        BankingCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());

        return mapper.toResponse(repository.save(category));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}