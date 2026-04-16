package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.exception.exceptions.AccountNotFoundException;
import esg.esgdocbuilder.exception.exceptions.BankOperationNotFoundException;
import esg.esgdocbuilder.mapper.BankOperationMapper;
import esg.esgdocbuilder.model.dto.AccountDTO;
import esg.esgdocbuilder.model.dto.request.BankOperationRequest;
import esg.esgdocbuilder.model.dto.response.BankOperationResponse;
import esg.esgdocbuilder.model.dto.response.PaginationResponse;
import esg.esgdocbuilder.model.entity.Account;
import esg.esgdocbuilder.model.entity.BankOperation;
import esg.esgdocbuilder.model.enums.TypeOfOperationEnums;
import esg.esgdocbuilder.repository.AccountRepository;
import esg.esgdocbuilder.repository.BankOperationRepository;
import esg.esgdocbuilder.service.BankOperationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BankOperationServiceImpl implements BankOperationService {

    private final BankOperationRepository bankOperationRepository;
    private final BankOperationMapper bankOperationMapper;
    private final AccountRepository accountRepository;

    @Override
    public BankOperationResponse createOperation(BankOperationRequest bankOperationRequest) {
        Account account = accountRepository.findByName(bankOperationRequest.getAccountName())
                .orElseThrow(() -> new AccountNotFoundException(ApiErrorMessage.ACCOUNT_NOT_FOUND.getMessage()));

        BankOperation bankOperation = bankOperationMapper.toEntity(bankOperationRequest, account);

        if (bankOperation.getType() == TypeOfOperationEnums.INCOME) {
            account.setBalance(account.getBalance().add(bankOperation.getAmount()));
        } else if (bankOperation.getType() == TypeOfOperationEnums.EXPENSE) {
            account.setBalance(account.getBalance().subtract(bankOperation.getAmount()));
        }

        accountRepository.save(account);
        BankOperation savedBankOperation = bankOperationRepository.save(bankOperation);
        return bankOperationMapper.toResponse(savedBankOperation);
    }

    @Override
    public PaginationResponse<BankOperationResponse> getAllOperations(Pageable pageable) {
        Page<BankOperation> bankOperations = bankOperationRepository.findAllByIsDeletedFalse(pageable);
        Page<BankOperationResponse> dtos = bankOperations.map(bankOperationMapper::toResponse);
        return new PaginationResponse<>(
                dtos.getContent(),
                new PaginationResponse.Pagination(
                        dtos.getTotalElements(),
                        pageable.getPageSize(),
                        dtos.getNumber() + 1,
                        dtos.getTotalPages()

                )
        );
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(bankOperationMapper::toDTO).toList();
    }

    @Transactional
    @Override
    public void deleteOperation(Long id) {
        BankOperation operation = bankOperationRepository.findById(id).orElseThrow(
                () -> new BankOperationNotFoundException(ApiErrorMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        operation.setDeleted(true);

    }
}
