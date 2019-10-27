package we.transfer.account.business.dto;

import java.math.BigDecimal;
import java.util.Date;

public class AccountDTO {
    private final long id;
    private final Date creationDate;
    private final BigDecimal balance;

    public AccountDTO(long id, Date creationDate, BigDecimal balance) {
        this.id = id;
        this.creationDate = creationDate;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
