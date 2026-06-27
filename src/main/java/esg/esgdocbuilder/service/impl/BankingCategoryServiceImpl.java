package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.exception.exceptions.BankCategoryNotFound;
import esg.esgdocbuilder.mapper.BankOperationMapper;
import esg.esgdocbuilder.model.dto.request.NewBankCategoryRequest;
import esg.esgdocbuilder.model.dto.response.BankCategoryResponse;
import esg.esgdocbuilder.model.entity.BankingCategory;
import esg.esgdocbuilder.repository.BankingCategoryRepository;
import esg.esgdocbuilder.service.BankingCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankingCategoryServiceImpl implements BankingCategoryService {

    private final BankingCategoryRepository repository;
    private final BankOperationMapper mapper;

    @Transactional
    @Override
    public BankCategoryResponse create(NewBankCategoryRequest request) {
        log.info("Creating banking category with name={}", request.getName());

        BankingCategory category = mapper.toEntity(request);
        BankingCategory saved = repository.save(category);

        log.info("Banking category created with id={}", saved.getId());

        return mapper.toResponse(saved);
    }

    @Override
    public List<BankCategoryResponse> getAll() {
        log.info("Fetching all banking categories");

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public BankCategoryResponse getById(Long id) {
        log.info("Fetching banking category by id={}", id);

        BankingCategory category = findEntityById(id);

        return mapper.toResponse(category);
    }

    @Transactional
    @Override
    public BankCategoryResponse update(Long id, NewBankCategoryRequest request) {
        log.info("Updating banking category id={}", id);

        BankingCategory category = findEntityById(id);

        category.setName(request.getName());

        // Hibernate dirty checking → save() не нужен
        return mapper.toResponse(category);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("Deleting banking category id={}", id);

        BankingCategory category = findEntityById(id);

        repository.delete(category);

        log.info("Banking category deleted id={}", id);
    }

    /**
     * Centralized entity fetching with error handling
     */
    private BankingCategory findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Banking category not found id={}", id);
                    return new BankCategoryNotFound(
                            "Banking category with id " + id + " not found"
                    );
                });
    }
}