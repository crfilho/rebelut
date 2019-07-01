package model;
import java.time.Instant;
import java.util.Objects;
import static util.IdGen.genTxID;

public class Transaction {
    private String id;
    private double sum;
    private long accountid;
    private TransactionType txType;
    private int status;
    private Instant time;

    public Transaction(TransactionType txType, double sum, long accountid) {

        this.id = genTxID();
        this.accountid = accountid;
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
        return accountid;
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
        if (o == null || getClass() != o.getClass())
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
}
