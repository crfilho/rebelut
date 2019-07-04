package validator;
import model.Transaction;
import model.Transfer;
import validator.exceptions.ErrorMessages;
import validator.exceptions.InvalidTransactionException;

import java.math.BigDecimal;

public class TransactionValidator implements ITransactionValidator {

    @Override
    public void validateTransfer (Transfer transfer) {

        validateTransaction(transfer.getOrigTransaction());
        validateTransaction(transfer.getDestTransaction());

        if (transfer.getOrigAccountId() == transfer.getDestAccountId())
            throw new InvalidTransactionException(ErrorMessages.INVALID_TRANSACTION);
    }

    @Override
    public void validateTransaction (Transaction transaction) throws InvalidTransactionException {

        validateAmount (transaction);
        validateBalance(transaction);
    }

    private void validateAmount (Transaction transaction) {

        if (transaction.isDeposit() || transaction.isTransferIn()) {

            if (transaction.getSum() <= 0)
                throw new InvalidTransactionException(ErrorMessages.INVALID_AMOUNT);
        }
        else if (transaction.isWithdraw() || transaction.isTransferOut()) {

            if (transaction.getSum() >= 0)
                throw new InvalidTransactionException(ErrorMessages.INVALID_AMOUNT);
        }
    }

    private void validateBalance (Transaction transaction) throws InvalidTransactionException {

        if (transaction.isWithdraw() || transaction.isTransferOut()) {

            BigDecimal balance = transaction.getAccount().getBalance();
            BigDecimal sum = BigDecimal.valueOf(Math.abs(transaction.getSum()));

            if (balance.compareTo(sum) < 0)
                throw new InvalidTransactionException(ErrorMessages.INSUFFICIENT_FUNDS);
        }
    }
}
