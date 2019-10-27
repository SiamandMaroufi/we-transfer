package we.transfer.account.business.dto;

import we.transfer.account.business.AccountTransactionType;

import java.math.BigDecimal;
import java.util.Date;

public class AccountTransactionDTO {
    private final long id;
    private final AccountTransactionType type;
    private final Long sourceAccountId;
    private final Long targetAccountId;
    private final Date createdDate;
    private final BigDecimal value;

    public AccountTransactionDTO(
            long id,
            AccountTransactionType type,
            Long sourceAccountId,
            Long targetAccountId,
            Date createdDate,
            BigDecimal value) {
        this.id = id;
        this.type = type;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.createdDate = createdDate;
        this.value = value;
    }

    public AccountTransactionType getType() {
        return type;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public long getId() {
        return id;
    }
}
