package we.transfer.account.business.persistance;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import we.transfer.account.business.AccountRepository;
import we.transfer.account.business.dto.AccountDTO;
import we.transfer.account.business.dto.AccountTransactionDTO;
import we.transfer.account.business.exception.AccountDoesNotExistedException;
import we.transfer.account.business.exception.NotEnoughBalanceException;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

class AccountRepositoryImpl implements AccountRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public AccountRepositoryImpl(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public long createAccount(BigDecimal inicialBalance) {
        final Account account = new Account();
        account.setBalance(inicialBalance);
        entityManager.persist(account);
        entityManager.flush();
        return account.getId();
    }

    @Override
    @Transactional
    public BigDecimal getBalance(long id) {
        return findOrFail(id).getBalance();
    }

    @Override
    @Transactional
    public void withdraw(long id, BigDecimal value) {
        final Account account = withdrawAccount(id, value);
        if (value.compareTo(account.getBalance()) > 0) {
            throw new NotEnoughBalanceException(id);
        }
        entityManager.persist(AccountTransaction.withdraw(account, value));
    }

    @Override
    @Transactional
    public void deposit(long id, BigDecimal value) {
        final Account account = depositAccount(id, value);
        persistTransaction(AccountTransaction.deposit(account, value));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<AccountTransactionDTO> findTransactions(long id) {
        final List result = entityManager
                .createQuery(
                        "SELECT new we.transfer.account.business.dto.AccountTransactionDTO(" +
                                "   t.id," +
                                "   t.transactionType," +
                                "   t.source.id," +
                                "   t.account.id," +
                                "   t.creationDate," +
                                "   t.value" +
                                ") FROM AccountTransaction t " +
                                "WHERE t.account.id = :id or t.source.id = :id", AccountTransactionDTO.class)
                .setParameter("id", id)
                .getResultList();
        return result;
    }

    @Override
    @Transactional
    public void transfer(long sourceId, long targetId, BigDecimal value) {
        final Account withdraw = withdrawAccount(sourceId, value);
        final Account deposit = depositAccount(targetId, value);

        persistTransaction(AccountTransaction.transfer(withdraw, deposit, value));
    }

    @Override
    public AccountDTO findAccount(long id) {
        final Account account = findOrFail(id);
        return new AccountDTO(account.getId(), account.getCreationDate(), account.getBalance());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    private Account withdrawAccount(long id, BigDecimal value) {
        final Account account = findForUpdate(id);
        if (account.getBalance().compareTo(value) < 0) {
            throw new NotEnoughBalanceException(account.getId());
        }
        account.setBalance(account.getBalance().subtract(value));
        entityManager.persist(account);
        return account;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    private Account depositAccount(long id, BigDecimal value) {
        final Account account = findForUpdate(id);
        account.setBalance(account.getBalance().add(value));
        entityManager.persist(account);
        return account;
    }

    private void persistTransaction(AccountTransaction transaction) {
        entityManager.persist(transaction);
    }

    @Transactional(readOnly = true)
    private Account findOrFail(long id) {
        final Account account = entityManager.find(Account.class, id, LockModeType.PESSIMISTIC_READ);
        if (account == null) {
            throw new AccountDoesNotExistedException(id);
        }
        return account;
    }

    private Account findForUpdate(long id) {
        final Account account = entityManager.find(Account.class, id, LockModeType.PESSIMISTIC_WRITE);
        if (account == null) {
            throw new AccountDoesNotExistedException(id);
        }
        return account;
    }
}
