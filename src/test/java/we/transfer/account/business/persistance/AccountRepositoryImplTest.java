package we.transfer.account.business.persistance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import we.transfer.account.business.AccountTransactionType;
import we.transfer.account.business.dto.AccountTransactionDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static we.transfer.account.business.AccountTransactionType.TRANSFER;

class AccountRepositoryImplTest {

    private EntityManager entityManager;
    private AccountRepositoryImpl repository;

    @BeforeEach
    void setup() {
        entityManager = mock(EntityManager.class);
        repository = new AccountRepositoryImpl(entityManager);
    }

    @Test
    void createAccount() {
        final ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        repository.createAccount(BigDecimal.TEN);
        verify(entityManager, times(1)).persist(argumentCaptor.capture());

        assertNotNull(argumentCaptor.getValue());
        assertEquals(argumentCaptor.getValue().getId(), 0);
        assertEquals(argumentCaptor.getValue().getBalance(), BigDecimal.TEN);
        assertNotNull(argumentCaptor.getValue().getCreationDate());
    }

    @Test
    void getTransactions() {
        TypedQuery<AccountTransactionDTO> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), anyLong())).thenReturn(typedQuery);
        final Account source = mock(Account.class);
        final Account target = mock(Account.class);
        Date date = new Date();
        final AccountTransactionDTO transaction = new AccountTransactionDTO(1L, TRANSFER, 1L, 2L, date, BigDecimal.TEN);
        final List<AccountTransactionDTO> list = singletonList(transaction);
        when(typedQuery.getResultList()).thenReturn(list);

        final List<AccountTransactionDTO> response = repository.findTransactions(1L);

        assertNotNull(response);
        assertNotNull(response.isEmpty());

        assertNotNull(response.get(0));
        assertEquals(response.get(0).getId(), 1L);
        assertEquals(response.get(0).getType(), TRANSFER);
        assertEquals(response.get(0).getSourceAccountId(), 1L);
        assertEquals(response.get(0).getTargetAccountId(), 2L);
        assertEquals(response.get(0).getCreatedDate(), date);
        assertEquals(response.get(0).getValue(), BigDecimal.TEN);
    }

    @Test
    void deposit() {
    }


    @Test
    void transfer() {
    }

    @Test
    void findAccount() {
    }
}