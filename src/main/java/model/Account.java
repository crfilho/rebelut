package model;
import java.math.BigDecimal;
import java.util.Objects;
import static util.IdGen.accGen;

public class Account {
    private Long id;
    private volatile BigDecimal balance;

    public Account() {

        this.id = accGen();
        this.setBalance(BigDecimal.ZERO);
    }

    public long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;

        Account that = (Account) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
