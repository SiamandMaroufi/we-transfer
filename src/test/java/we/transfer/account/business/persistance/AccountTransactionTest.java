package we.transfer.account.business.persistance;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static we.transfer.account.business.AccountTransactionType.*;

class AccountTransactionTest {

    @Test
    void withdraw() {
        final Account account = mock(Account.class);
        final AccountTransaction transaction = AccountTransaction.withdraw(account, BigDecimal.TEN);
        assertNotNull(transaction);
        assertEquals(WITHDRAW, transaction.getTransactionType());
        assertEquals(0, transaction.getId());
        assertEquals(account, transaction.getAccount());
        assertNull(transaction.getSource());
        assertNotNull(transaction.getCreationDate());
        assertEquals(BigDecimal.TEN, transaction.getValue());
    }

    @Test
    void deposit() {
        final Account account = mock(Account.class);
        final AccountTransaction transaction = AccountTransaction.deposit(account, BigDecimal.TEN);
        assertNotNull(transaction);
        assertEquals(DEPOSIT, transaction.getTransactionType());
        assertEquals(0, transaction.getId());
        assertEquals(account, transaction.getAccount());
        assertNull(transaction.getSource());
        assertEquals(BigDecimal.TEN, transaction.getValue());
    }

    @Test
    void transfer() {
        final Account account = mock(Account.class);
        final Account source = mock(Account.class);

        final AccountTransaction transaction = AccountTransaction.transfer(source, account, BigDecimal.TEN);
        assertNotNull(transaction);
        assertEquals(TRANSFER, transaction.getTransactionType());
        assertEquals(0, transaction.getId());
        assertEquals(account, transaction.getAccount());
        assertEquals(source, transaction.getSource());
        assertEquals(BigDecimal.TEN, transaction.getValue());
    }


}