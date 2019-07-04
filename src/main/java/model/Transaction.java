package model;
import java.time.Instant;
import java.util.Objects;

import static util.IdGen.genTxID;

public class Transaction {
    private String id;
    private double sum;
    private long accountid;
    private TransactionType type;
    private int status;
    private String time;
    private transient Account account;

    public Transaction(TransactionType type, double sum, Account account) {

        this.id = genTxID();
        this.type = type;
        this.account = account;
        this.accountid = account.getId();
        this.time = Instant.now().toString();
        this.sum = sum;
        this.setStatus(0);
    }

    public String getId() {
        return id;
    }

    public double getSum() {
        return sum;
    }

    public long getAccountId() {
        return account.getId();
    }

    public String getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TransactionType getType() {
        return type;
    }
    public Account getAccount() { return account; }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Transaction))
            return false;

        Transaction that = (Transaction) o;
        return this.getAccountId()==that.getAccountId()
                && this.getSum()==that.getSum()
                && this.getTime()==that.getTime();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getAccountId(), getSum(), getTime());
    }

    public boolean isWithdraw() {

        return this.getType().equals(TransactionType.WITHDRAW);
    }

    public boolean isDeposit() {

        return this.getType().equals(TransactionType.DEPOSIT);
    }

    public boolean isTransferIn() {

        return this.getType().equals(TransactionType.TRANSFER_IN);
    }

    public boolean isTransferOut() {

        return this.getType().equals(TransactionType.TRANSFER_OUT);
    }
}
