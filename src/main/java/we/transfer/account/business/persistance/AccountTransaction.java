package we.transfer.account.business.persistance;

import we.transfer.account.business.AccountTransactionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
class AccountTransaction {
    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private AccountTransactionType transactionType;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY, optional = false)
    private Account account;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY, optional = true)
    private Account source;

    private BigDecimal value;

    private Date creationDate = new Date();

    public AccountTransaction() {
        // hibernate
    }

    private AccountTransaction(AccountTransactionType transactionType, Account source, Account target, BigDecimal value) {
        this.transactionType = transactionType;
        this.source = source;
        this.account = target;
        this.value = value;
    }

    public static AccountTransaction withdraw(Account account, BigDecimal value) {
        return new AccountTransaction(AccountTransactionType.WITHDRAW, null, account, value);
    }

    public static AccountTransaction deposit(Account account, BigDecimal value) {
        return new AccountTransaction(AccountTransactionType.DEPOSIT, null, account, value);
    }

    public static AccountTransaction transfer(Account source, Account target, BigDecimal value) {
        return new AccountTransaction(AccountTransactionType.TRANSFER, source, target, value);
    }


    public long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public Account getSource() {
        return source;
    }

    public BigDecimal getValue() {
        return value;
    }

    public AccountTransactionType getTransactionType() {
        return transactionType;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
