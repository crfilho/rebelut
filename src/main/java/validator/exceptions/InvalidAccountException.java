package validator.exceptions;

public class InvalidAccountException extends RuntimeException {

    public InvalidAccountException() {
        super(ErrorMessages.INVALID_ACCOUNT);
    }
}
