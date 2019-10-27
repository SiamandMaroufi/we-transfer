package we.transfer.account.api;

import io.micronaut.http.annotation.*;
import we.transfer.account.api.vm.AccountResponseVM;
import we.transfer.account.api.vm.DepositRequestVM;
import we.transfer.account.api.vm.AccountTransactionVM;
import we.transfer.account.api.vm.WithdrawRequestVM;
import we.transfer.account.business.dto.AccountDTO;
import we.transfer.account.business.AccountService;
import we.transfer.account.business.dto.AccountTransactionDTO;

import java.math.BigDecimal;
import java.util.List;

@Controller("/accounts")
public class AccountController {

    private final AccountService service;
    private final AccountMapper mapper;

    public AccountController(AccountService service, AccountMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Get("/{id}")
    public AccountResponseVM get(@PathVariable("id") long id) {
        final AccountDTO account = service.findOne(id);
        return mapper.map(account);
    }

    @Get("/{id}/balance")
    public BigDecimal getBalance(@PathVariable("id") final long accountId) {
        return service.getBalance(accountId);
    }

    @Get("/{id}/transactions")
    public List<AccountTransactionVM> getTransactions(@PathVariable("id") final long accountId) {
        final List<AccountTransactionDTO> transactions = service.getAccountTransactions(accountId);
        return mapper.mapTransactions(transactions);
    }

    @Post("/new")
    public long create() {
        return service.create(new BigDecimal(0));
    }

    @Put("/deposit")
    public void deposit(DepositRequestVM request) {
        service.deposit(request.getId(), request.getValue());
    }

    @Put("/withdraw")
    public void withdraw(WithdrawRequestVM request) {
        service.withdraw(request.getId(), request.getValue());
    }


}
