package validator;
import model.Account;
import model.Transaction;
import model.Transfer;

public interface ITransactionValidator {
    void validateAccount (Account account);
    void validateTransfer (Transfer transfer);
    void validateTransaction (Transaction transaction);
}
