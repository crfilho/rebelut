package model;
import java.math.BigDecimal;
import java.util.Objects;

import static util.IdGen.genAccID;

public class Account {
    private Long id;
    private volatile BigDecimal balance;

    public Account() {

        this.id = genAccID();
        this.balance = BigDecimal.ZERO;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void sum(double amount) {
        this.balance = balance.add(BigDecimal.valueOf(amount));
    }

    public void subtract(double amount) {

        this.balance = balance.subtract(BigDecimal.valueOf(amount));
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
