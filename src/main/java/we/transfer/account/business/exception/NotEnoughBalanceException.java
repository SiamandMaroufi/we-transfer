package we.transfer.account.business.exception;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException(long id) {
        super(String.format("not enough balance for account #%d ", id));
    }
}
