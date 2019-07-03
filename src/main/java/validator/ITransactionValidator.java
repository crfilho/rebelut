package validator;
import model.Transaction;

public interface ITransactionValidator {
    void validate(Transaction transaction) ;
    void validateTransfer(Transaction origTransaction, Transaction destTransaction) ;
}
