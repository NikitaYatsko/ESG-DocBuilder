package esg.esgdocbuilder.mapper;

import esg.esgdocbuilder.model.dto.AccountDTO;
import esg.esgdocbuilder.model.dto.request.BankOperationRequest;
import esg.esgdocbuilder.model.dto.response.BankOperationResponse;
import esg.esgdocbuilder.model.entity.Account;
import esg.esgdocbuilder.model.entity.BankOperation;
import org.springframework.stereotype.Component;

@Component
public class BankOperationMapper {
    public BankOperationResponse toResponse(BankOperation bankOperation) {
        return BankOperationResponse.builder()
                .id(bankOperation.getId())
                .type(bankOperation.getType())
                .amount(bankOperation.getAmount())
                .accountName(bankOperation.getAccount().getName())
                .comment(bankOperation.getComment())
                .createdAt(bankOperation.getCreatedAt())
                .build();
    }

    public BankOperation toEntity(BankOperationRequest bankOperationRequest, Account account) {
        return BankOperation.builder()
                .type(bankOperationRequest.getType())
                .amount(bankOperationRequest.getAmount())
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
}
