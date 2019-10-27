package we.transfer.account.business;

import we.transfer.account.business.dto.AccountDTO;
import we.transfer.account.business.dto.AccountTransactionDTO;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;

@Singleton
public class AccountService {
    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public long create(BigDecimal balance) {
        return repository.createAccount(balance);
    }

    public void withdraw(long id, BigDecimal value) {
        repository.withdraw(id, value);
    }

    public void deposit(long id, BigDecimal value) {
        repository.deposit(id, value);
    }

    public void transfer(long sourceId, long targetId, BigDecimal value) {
        repository.transfer(sourceId, targetId, value);
    }

    public BigDecimal getBalance(final long accountId) {
        return repository.getBalance(accountId);
    }

    public AccountDTO findOne(long id) {
        return repository.findAccount(id);
    }

    public List<AccountTransactionDTO> getAccountTransactions(final long id) {
        return repository.findTransactions(id);
    }
}
