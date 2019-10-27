package we.transfer.account.business.persistance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
class Account {

    @Id
    @GeneratedValue
    private long id;
    private Date createDate = new Date();
    private BigDecimal balance;

    public long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getCreationDate() {
        return createDate;
    }
}
