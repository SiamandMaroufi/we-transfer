package we.transfer.account.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import we.transfer.account.api.vm.AccountResponseVM;
import we.transfer.account.api.vm.AccountTransactionVM;
import we.transfer.account.business.dto.AccountDTO;
import we.transfer.account.business.dto.AccountTransactionDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static we.transfer.account.business.AccountTransactionType.*;


class AccountMapperTest {

    private AccountMapper mapper;

    private final Date depositDate = new Date();
    private final Date withdrawDate = new Date();
    private final Date transferDate = new Date();

    private final AccountTransactionDTO deposit = new AccountTransactionDTO(1L, DEPOSIT, null, 100L, depositDate, BigDecimal.TEN);
    private final AccountTransactionDTO withdraw = new AccountTransactionDTO(2L, WITHDRAW, null, 101L, withdrawDate, BigDecimal.valueOf(15));
    private final AccountTransactionDTO transfer = new AccountTransactionDTO(3L, TRANSFER, 104L, 101L, transferDate, BigDecimal.valueOf(100));


    @BeforeEach
    void setup() {
        mapper = new AccountMapper();
    }

    @Test
    void map() {
        final AccountDTO mockedAccount = mock(AccountDTO.class);
        when(mockedAccount.getId()).thenReturn(1L);
        when(mockedAccount.getBalance()).thenReturn(BigDecimal.TEN);

        final AccountResponseVM vm = mapper.map(mockedAccount);

        assertEquals(1L, vm.getId());
        assertEquals(BigDecimal.TEN, vm.getBalance());
    }

    @Test
    void mapTransactions_withEmptyInput_returnsEmpty() {
        final List<AccountTransactionVM> results = mapper.mapTransactions(emptyList());
        assertTrue(results.isEmpty());
    }

    @Test
    void mapTransactions_withAList_returnsMappedItemsInOrder() {

        final List<AccountTransactionDTO> list = new ArrayList<>();
        list.add(deposit);
        list.add(withdraw);
        list.add(transfer);

        final List<AccountTransactionVM> result = mapper.mapTransactions(list);

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(1L, result.get(0).getTransactionId());
        assertEquals(2L, result.get(1).getTransactionId());
        assertEquals(3L, result.get(2).getTransactionId());

    }

    @Test
    void mapTransactions_depositTransaction_returnProperlyMappedVM() {
        final List<AccountTransactionDTO> list = singletonList(deposit);

        final List<AccountTransactionVM> result = mapper.mapTransactions(list);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getTransactionId());
        assertEquals(DEPOSIT, result.get(0).getType());
        assertEquals(depositDate, result.get(0).getCreatedDate());
        assertEquals(BigDecimal.TEN, result.get(0).getValue());
        assertNull(result.get(0).getSourceAccount());
    }

    @Test
    void mapTransactions_withdrawTransaction_returnProperlyMappedVM() {
        final List<AccountTransactionDTO> list = singletonList(withdraw);

        final List<AccountTransactionVM> result = mapper.mapTransactions(list);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2L, result.get(0).getTransactionId());
        assertEquals(WITHDRAW, result.get(0).getType());
        assertEquals(withdrawDate, result.get(0).getCreatedDate());
        assertEquals(BigDecimal.valueOf(15), result.get(0).getValue());
        assertNull(result.get(0).getSourceAccount());
    }

    @Test
    void mapTransactions_transferTransaction_returnProperlyMappedVM() {
        final List<AccountTransactionDTO> list = singletonList(transfer);

        final List<AccountTransactionVM> result = mapper.mapTransactions(list);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3L, result.get(0).getTransactionId());
        assertEquals(TRANSFER, result.get(0).getType());
        assertEquals(transferDate, result.get(0).getCreatedDate());
        assertEquals(BigDecimal.valueOf(100), result.get(0).getValue());
        assertEquals(104, result.get(0).getSourceAccount());
    }

}