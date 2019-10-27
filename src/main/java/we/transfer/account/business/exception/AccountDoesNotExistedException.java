package we.transfer.account.business.exception;

public class AccountDoesNotExistedException extends RuntimeException {
    public AccountDoesNotExistedException(long id) {
        super(String.format("account #%d does not existed", id));
    }
}
