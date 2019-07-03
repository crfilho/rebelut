package validator;
import model.Transaction;
import model.TransactionType;

import java.math.BigDecimal;

public class BasicTransactionValidator implements ITransactionValidator {

    public void validate(Transaction transaction) {

        if (transaction.getAccount() == null) {

            if (transaction.isTransferOut())
                throw new InvalidTransactionException(TransactionErrorMessage.INVALID_ORIG_ACCOUNT);
            else if (transaction.isTransferIn())
                throw new InvalidTransactionException(TransactionErrorMessage.INVALID_DEST_ACCOUNT);
            else
                throw new InvalidTransactionException(TransactionErrorMessage.INVALID_ACCOUNT);
        }

        if (transaction.isDeposit() || transaction.isTransferIn()) {

            if (transaction.getSum() <= 0)
                throw new InvalidTransactionException(TransactionErrorMessage.INVALID_AMOUNT);
        }
        else if (transaction.isWithdraw() || transaction.isTransferOut()) {

            if (transaction.getSum() >= 0)
                throw new InvalidTransactionException(TransactionErrorMessage.INVALID_AMOUNT);
        }

        if (transaction.isWithdraw() || transaction.isTransferOut()) {

            BigDecimal balance = transaction.getAccount().getBalance();
            BigDecimal sum = BigDecimal.valueOf(Math.abs(transaction.getSum()));

            if (balance.compareTo(sum) < 0)
                throw new InvalidTransactionException(TransactionErrorMessage.INSUFFICIENT_FUNDS);
        }
    }

    public void validateTransfer (Transaction origTransaction, Transaction destTransaction) {

        validate (origTransaction);
        validate (destTransaction);

        if (origTransaction.getAccount().equals(destTransaction.getAccount()))
            throw new InvalidTransactionException(TransactionErrorMessage.INVALID_TRANSACTION);
    }
}
