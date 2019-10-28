package we.transfer.account.business;

import we.transfer.account.business.dto.AccountDTO;
import we.transfer.account.business.dto.AccountTransactionDTO;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository {
    long createAccount(BigDecimal initialBalance);

    BigDecimal getBalance(long id);

    void withdraw(long id, BigDecimal value);

    void deposit(long id, BigDecimal value);

    List<AccountTransactionDTO> findTransactions(long id);

    void transfer(long sourceId, long targetId, BigDecimal value);

    AccountDTO findAccount(long id);
}
