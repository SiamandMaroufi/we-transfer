package we.transfer.account.api;

import we.transfer.account.api.vm.AccountResponseVM;
import we.transfer.account.api.vm.AccountTransactionVM;
import we.transfer.account.business.dto.AccountDTO;
import we.transfer.account.business.dto.AccountTransactionDTO;

import javax.inject.Singleton;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Singleton
public class AccountMapper {

    public AccountResponseVM map(AccountDTO account) {
        return new AccountResponseVM(account.getId(), account.getBalance());
    }

    public List<AccountTransactionVM> mapTransactions(List<AccountTransactionDTO> accountTransactions) {
        return accountTransactions
                .stream()
                .map(this::mapTransaction)
                .collect(toList());
    }

    private AccountTransactionVM mapTransaction(AccountTransactionDTO accountTransactionDTO) {
        return new AccountTransactionVM(
                accountTransactionDTO.getId(),
                accountTransactionDTO.getType(),
                accountTransactionDTO.getCreatedDate(),
                accountTransactionDTO.getSourceAccountId(),
                accountTransactionDTO.getValue()
        );

    }
}
