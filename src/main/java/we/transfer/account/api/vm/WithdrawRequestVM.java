package we.transfer.account.api.vm;

import java.math.BigDecimal;

public class WithdrawRequestVM {
    private long id;
    private BigDecimal value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
