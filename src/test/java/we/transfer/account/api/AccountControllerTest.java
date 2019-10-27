package we.transfer.account.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import we.transfer.account.api.vm.AccountResponseVM;
import we.transfer.account.api.vm.AccountTransactionVM;
import we.transfer.account.api.vm.DepositRequestVM;
import we.transfer.account.api.vm.WithdrawRequestVM;
import we.transfer.account.business.AccountService;
import we.transfer.account.business.dto.AccountDTO;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    private AccountController controller;
    private AccountService service;
    private AccountMapper mapper;

    @BeforeEach
    void setUp() {
        service = mock(AccountService.class);
        mapper = mock(AccountMapper.class);
        controller = new AccountController(service, mapper);
    }

    @Test
    void get() {
        final AccountDTO account = mock(AccountDTO.class);
        final AccountResponseVM response = mock(AccountResponseVM.class);

        when(service.findOne(1L)).thenReturn(account);
        when(mapper.map(account)).thenReturn(response);

        final AccountResponseVM result = controller.get(1L);

        assertEquals(response, result);
    }

    @Test
    void getBalance() {
        final BigDecimal mockedBalance = mock(BigDecimal.class);

        when(service.getBalance(1L)).thenReturn(mockedBalance);

        final BigDecimal response = controller.getBalance(1L);

        assertEquals(response, mockedBalance);
    }

    @Test
    void getTransactions() {
        final List mockedList = mock(List.class);
        final List mockedList2 = mock(List.class);

        when(service.getAccountTransactions(1L)).thenReturn(mockedList);
        when(mapper.mapTransactions(mockedList)).thenReturn(mockedList2);

        final List<AccountTransactionVM> response = controller.getTransactions(1L);

        assertEquals(response, mockedList2);
    }

    @Test
    void create() {
        controller.create();

        verify(service, times(1)).create(BigDecimal.ZERO);
    }

    @Test
    void deposit() {
        final DepositRequestVM request = new DepositRequestVM();
        request.setId(1L);
        request.setValue(BigDecimal.TEN);

        controller.deposit(request);

        verify(service, times(1)).deposit(1L, BigDecimal.TEN);
    }

    @Test
    void withdraw() {
        final WithdrawRequestVM request = new WithdrawRequestVM();
        request.setId(1L);
        request.setValue(BigDecimal.TEN);

        controller.withdraw(request);

        verify(service, times(1)).withdraw(1L, BigDecimal.TEN);
    }
}