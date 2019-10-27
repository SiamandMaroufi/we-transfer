package we.transfer.account.api.vm;

import java.math.BigDecimal;

public class AccountResponseVM {
    private final long id;
    private final BigDecimal balance;

    public AccountResponseVM(long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
