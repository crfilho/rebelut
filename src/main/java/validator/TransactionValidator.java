package validator;
import model.Account;
import model.Transaction;
import model.Transfer;

import java.math.BigDecimal;

public class TransactionValidator implements ITransactionValidator {

    @Override
    public void validateTransfer (Transfer transfer) {

        validateTransaction(transfer.getOrigTransaction());
        validateTransaction(transfer.getDestTransaction());

        if (transfer.getOrigAccountId() == transfer.getDestAccountId())
            throw new InvalidTransactionException(TransactionErrorMessage.INVALID_TRANSACTION);
    }

    @Override
    public void validateTransaction (Transaction transaction) throws InvalidTransactionException {

        Account account = transaction.getAccount();

        if (account == null) {

            if (transaction.isTransferOut())
                throw new InvalidTransactionException(TransactionErrorMessage.INVALID_ORIG_ACCOUNT);
            else if (transaction.isTransferIn())
                throw new InvalidTransactionException(TransactionErrorMessage.INVALID_DEST_ACCOUNT);
            else
                throw new InvalidTransactionException(TransactionErrorMessage.INVALID_ACCOUNT);
        }

        validateAmount (transaction);

        if (transaction.isWithdraw() || transaction.isTransferOut())
            validateBalance(transaction);
    }

    @Override
    public void validateAccount (Account account) throws InvalidTransactionException {

        if (account == null)
            throw new InvalidTransactionException(TransactionErrorMessage.INVALID_ACCOUNT);
    }

    private void validateAmount (Transaction transaction) {

        if (transaction.isDeposit() || transaction.isTransferIn()) {

            if (transaction.getSum() <= 0)
                throw new InvalidTransactionException(TransactionErrorMessage.INVALID_AMOUNT);
        }
        else if (transaction.isWithdraw() || transaction.isTransferOut()) {

            if (transaction.getSum() >= 0)
                throw new InvalidTransactionException(TransactionErrorMessage.INVALID_AMOUNT);
        }
    }

    private void validateBalance (Transaction transaction) throws InvalidTransactionException {

        Account account = transaction.getAccount();

        BigDecimal balance = account.getBalance();
        BigDecimal sum = BigDecimal.valueOf(Math.abs(transaction.getSum()));

        if (balance.compareTo(sum) < 0)
            throw new InvalidTransactionException(TransactionErrorMessage.INSUFFICIENT_FUNDS);
    }
}
