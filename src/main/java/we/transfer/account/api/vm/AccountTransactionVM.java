package we.transfer.account.api.vm;

import we.transfer.account.business.AccountTransactionType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AccountTransactionVM implements Serializable {
    private final long transactionId;
    private final AccountTransactionType type;
    private final Date createdDate;
    private final Long sourceAccount;
    private final BigDecimal value;

    public AccountTransactionVM(
            long transactionId,
            AccountTransactionType type,
            Date createdDate,
            Long sourceAccount,
            BigDecimal value
    ) {
        this.transactionId = transactionId;
        this.type = type;
        this.createdDate = createdDate;
        this.sourceAccount = sourceAccount;
        this.value = value;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public AccountTransactionType getType() {
        return type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Long getSourceAccount() {
        return sourceAccount;
    }

    public BigDecimal getValue() {
        return value;
    }
}
