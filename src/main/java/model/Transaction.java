package model;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static util.IdGen.genTxID;

public class Transaction {
    private String id;
    private double sum;
    private Account account;
    private TransactionType txType;
    private int status;
    private Instant time;

    public Transaction(TransactionType txType, double sum, Account account) {

        this.id = genTxID();
        this.txType = txType;
        this.account = account;
        this.time = Instant.now();
        this.sum = sum;
        this.setStatus(0);
    }

    public String getId() {
        return id;
    }

    public double getSum() {
        return sum;
    }

    public Account getAccount() {
        return account;
    }

    public Instant getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TransactionType getTxType() {
        return txType;
    }

    public void setTxType(TransactionType txType) {
        this.txType = txType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Transaction))
            return false;

        Transaction that = (Transaction) o;
        return this.account.equals(that.getAccount())
                && this.getSum()==that.getSum()
                && this.getTime()==that.getTime();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccount().getId(), getSum(), getTime());
    }

    public boolean isWithdraw() { return this.txType.equals(TransactionType.WITHDRAW); }

    public boolean isDeposit() {
        return this.txType.equals(TransactionType.DEPOSIT); }

    public boolean isTransferIn() {
        return this.txType.equals(TransactionType.TRANSFER_IN); }

    public boolean isTransferOut() {
        return this.txType.equals(TransactionType.TRANSFER_OUT); }
}
