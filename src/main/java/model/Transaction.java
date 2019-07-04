package model;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static util.IdGen.genTxID;

public class Transaction {
    private String id;
    private double sum;
    private long accountid;
    private TransactionType txType;
    private int status;
    private Instant time;
    private transient Account account;

    public Transaction(TransactionType txType, double sum, Account account) {

        this.id = genTxID();
        this.txType = txType;
        this.account = account;
        //this.accountid = account?.getId();
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

    public long getAccountId() {
        return account.getId();
    }

    public Instant getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }

    public String getTest() {
        return "BLABLA";
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

        return this.txType.equals(TransactionType.WITHDRAW);
    }

    public boolean isDeposit() {

        return this.txType.equals(TransactionType.DEPOSIT);
    }

    public boolean isTransferIn() {

        return this.txType.equals(TransactionType.TRANSFER_IN);
    }

    public boolean isTransferOut() {

        return this.txType.equals(TransactionType.TRANSFER_OUT);
    }
}
