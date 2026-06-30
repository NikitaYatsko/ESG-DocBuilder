package esg.esgdocbuilder.mapper;

import esg.esgdocbuilder.model.dto.AccountDTO;
import esg.esgdocbuilder.model.dto.request.BankOperationRequest;
import esg.esgdocbuilder.model.dto.request.NewBankCategoryRequest;
import esg.esgdocbuilder.model.dto.response.BankCategoryResponse;
import esg.esgdocbuilder.model.dto.response.BankOperationResponse;
import esg.esgdocbuilder.model.entity.Account;
import esg.esgdocbuilder.model.entity.BankOperation;
import esg.esgdocbuilder.model.entity.BankingCategory;
import org.springframework.stereotype.Component;

@Component
public class BankOperationMapper {
    public BankOperationResponse toResponse(BankOperation bankOperation) {
        return BankOperationResponse.builder()
                .id(bankOperation.getId())
                .type(bankOperation.getType())
                .category(bankOperation.getCategory().getName())
                .amount(bankOperation.getAmount())
                .accountName(bankOperation.getAccount().getName())
                .comment(bankOperation.getComment())
                .createdAt(bankOperation.getCreatedAt())
                .build();
    }

    public BankOperation toEntity(BankingCategory category, BankOperationRequest bankOperationRequest, Account account) {
        return BankOperation.builder()
                .type(bankOperationRequest.getType())
                .amount(bankOperationRequest.getAmount())
                .category(category)
                .account(account)
                .comment(bankOperationRequest.getComment())
                .build();
    }

    public AccountDTO toDTO(Account account) {
        return AccountDTO.builder()
                .name(account.getName())
                .balance(account.getBalance())
                .build();
    }

    public BankingCategory toEntity(NewBankCategoryRequest request) {
        BankingCategory category = new BankingCategory();
        category.setName(request.getName());
        category.setType(request.getType());
        return category;
    }

    public BankCategoryResponse toResponse(BankingCategory category) {
        BankCategoryResponse response = new BankCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setType(category.getType());
        return response;
    }



}
