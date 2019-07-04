package model;
public class Transfer {

    private transient long origAccountId;
    private transient long destAccountId;

    private Transaction origTransaction;
    private Transaction destTransaction;

    public Transfer(double amount, Account origAccount, Account destAccount) {
        //this.origAccountId = origAccount.getId();
        //this.destAccountId = destAccount.getId();

        origTransaction = new Transaction(TransactionType.TRANSFER_OUT, amount * -1, origAccount);
        destTransaction = new Transaction(TransactionType.TRANSFER_IN, amount, destAccount);
    }

    public long getOrigAccountId() {
        return origTransaction.getAccount().getId();
    }

    public long getDestAccountId() {
        return destTransaction.getAccount().getId();
    }

    public Transaction getDestTransaction() {
        return destTransaction;
    }

    public Transaction getOrigTransaction() {
        return origTransaction;
    }
}
