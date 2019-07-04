package validator;
import model.Account;
import model.Transaction;
import model.Transfer;

public interface ITransactionValidator {
    void validateTransfer (Transfer transfer);
    void validateTransaction (Transaction transaction);
}
