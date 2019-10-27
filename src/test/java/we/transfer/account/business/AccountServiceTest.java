package we.transfer.account.business;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import we.transfer.Application;
import we.transfer.account.business.dto.AccountDTO;
import we.transfer.account.business.dto.AccountTransactionDTO;
import we.transfer.account.business.exception.AccountDoesNotExistedException;
import we.transfer.account.business.exception.NotEnoughBalanceException;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static we.transfer.account.business.AccountTransactionType.*;

@Execution(ExecutionMode.CONCURRENT)
@MicronautTest(application = Application.class, propertySources = "application.properties")
class AccountServiceTest {

    @Inject
    AccountService service;

    private long account1Id;
    private long account2Id;
    private long account3Id;

    void ensureTestAccounts() {
        if (account1Id != 0) {
            return;
        }
        account1Id = service.create(BigDecimal.valueOf(100));
        account2Id = service.create(BigDecimal.ZERO);
        account3Id = service.create(BigDecimal.valueOf(1000));
    }

    @Test
    void initialBalance() {
        final BigDecimal balance = BigDecimal.valueOf(100);
        final long accountId = service.create(balance);
        final BigDecimal fetchedBalance = service.getBalance(accountId);

        assertEquals(balance, fetchedBalance);
    }

    @RepeatedTest(100)
    void transfer_repeatedly_wontThrowsException() {
        ensureTestAccounts();
        assertDoesNotThrow(() -> service.transfer(account1Id, account2Id, BigDecimal.ONE));
    }

    @RepeatedTest(100)
    void withdrawing_repeatedly_wontThrowsException() {
        ensureTestAccounts();
        assertDoesNotThrow(() -> service.withdraw(account3Id, BigDecimal.valueOf(10)));
    }

    @Test
    void withdrawing_whenItGetsEmpty_willThrowsException() {
        final long accountId = service.create(BigDecimal.TEN);
        assertThrows(NotEnoughBalanceException.class, () -> service.withdraw(accountId, BigDecimal.valueOf(10)));
    }


    @Test
    void withdrawing_whenAccountDoesNotExists_willThrowsAccountDoesNotExistedException() {
        assertThrows(AccountDoesNotExistedException.class, () -> service.withdraw(2132132131, BigDecimal.valueOf(10)));
    }

    @Test
    void deposit_withEmptyAccount_increasesTheBalanceToTheValue() {
        final long id = service.create(BigDecimal.ZERO);
        final BigDecimal value = BigDecimal.valueOf(1000000000);
        service.deposit(id, value);
        final BigDecimal balance = service.getBalance(id);
        assertEquals(balance, value);
    }

    @Test
    void withdraw_withAccountThatHasBalance_works() {
        final long id = service.create(BigDecimal.valueOf(1000));
        service.withdraw(id, BigDecimal.valueOf(500));
        final BigDecimal balance = service.getBalance(id);
        assertEquals(BigDecimal.valueOf(500), balance);
    }

    @Test
    void withdraw_whenAccountHasNoBalance_throwsWithdrawFailedException() {
        final long accountId = service.create(BigDecimal.ZERO);
        assertThrows(NotEnoughBalanceException.class, () -> service.withdraw(accountId, BigDecimal.TEN));
    }

    @Test
    void withdraw_whenAccountDoesNotExists_ThrowsAccountDoesNotExistedException() {
        assertThrows(AccountDoesNotExistedException.class, () -> service.findOne(1000000000));
    }

    @Test
    void withdraw_withEmptyAccount_throwsException() {
        final long id = service.create(BigDecimal.ZERO);
        assertThrows(NotEnoughBalanceException.class, () -> service.withdraw(id, BigDecimal.valueOf(100)));
    }

    @Test
    void transfer_withSafeValue_works() {
        final long idA = service.create(BigDecimal.valueOf(100));
        final long idB = service.create(BigDecimal.valueOf(500));
        service.transfer(idA, idB, BigDecimal.valueOf(50));
        service.transfer(idB, idA, BigDecimal.valueOf(550));
        service.transfer(idA, idB, BigDecimal.valueOf(200));

        assertEquals(BigDecimal.valueOf(400), service.getBalance(idA));
        assertEquals(BigDecimal.valueOf(200), service.getBalance(idB));
    }

    @Test
    void transfer_moreThanSourceAccountBalance_ThrowsException() {
        final long idA = service.create(BigDecimal.valueOf(100));
        final long idB = service.create(BigDecimal.valueOf(500));
        service.transfer(idA, idB, BigDecimal.valueOf(50));
        service.transfer(idB, idA, BigDecimal.valueOf(550));
        assertThrows(NotEnoughBalanceException.class, () -> service.transfer(idB, idA, BigDecimal.valueOf(201)));
    }

    @Test
    void findOne() {
        final long accountId = service.create(BigDecimal.valueOf(123));
        final AccountDTO account = service.findOne(accountId);

        assertEquals(accountId, account.getId());
        assertEquals(BigDecimal.valueOf(123), account.getBalance());
        assertNotNull(account.getCreationDate());
    }

    @Test
    void findOne_whenRecordNotExists_itShouldThrowsAccountDoesNotExistedException() {
        assertThrows(AccountDoesNotExistedException.class, () -> service.findOne(1000000));
    }

    @Test
    void getAccountTransactions() {
        final long accountId = service.create(BigDecimal.valueOf(10));
        final long accountId2 = service.create(BigDecimal.valueOf(10));

        service.deposit(accountId, BigDecimal.valueOf(500));
        service.withdraw(accountId, BigDecimal.valueOf(100));
        service.transfer(accountId, accountId2, BigDecimal.valueOf(400));

        List<AccountTransactionDTO> list = service.getAccountTransactions(accountId);
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(3, list.size());


        final AccountTransactionDTO depositTransaction = list.get(0);
        assertEquals(DEPOSIT, depositTransaction.getType());
        assertEquals(500L, depositTransaction.getValue().longValue());
        assertNull(depositTransaction.getSourceAccountId());
        assertEquals(accountId, depositTransaction.getTargetAccountId());

        final AccountTransactionDTO withdrawTransaction = list.get(1);
        assertEquals(WITHDRAW, withdrawTransaction.getType());
        assertEquals(100, withdrawTransaction.getValue().longValue());
        assertNull(withdrawTransaction.getSourceAccountId());
        assertEquals(accountId, withdrawTransaction.getTargetAccountId());


        final AccountTransactionDTO transferTransaction = list.get(2);
        assertEquals(TRANSFER, transferTransaction.getType());
        assertEquals(400, transferTransaction.getValue().longValue());
        assertEquals(accountId, transferTransaction.getSourceAccountId());
        assertEquals(accountId2, transferTransaction.getTargetAccountId());

    }

}